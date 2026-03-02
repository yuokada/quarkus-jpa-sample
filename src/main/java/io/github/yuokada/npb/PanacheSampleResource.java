package io.github.yuokada.npb;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/v1/panache-sample")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PanacheSampleResource {

    @Inject
    TeamRepository teamRepository;

    @Inject
    EntityManager entityManager;

    @GET
    @Path("/teams")
    public Response listTeamsByPanache(
        @QueryParam("name") String name,
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("size") @DefaultValue("5") int size
    ) {
        var query = teamRepository.find("isActive", Sort.by("id").ascending(), true);
        if (name != null && !name.isBlank()) {
            query = teamRepository.find(
                "isActive = ?1 and lower(name) like ?2",
                Sort.by("id").ascending(),
                true,
                "%" + name.toLowerCase() + "%"
            );
        }

        List<Team> teams = query.page(Page.of(page, size)).list();
        long total = query.count();

        return Response.ok(new TeamPageResponse(page, size, total, teams)).build();
    }

    @GET
    @Path("/teams/native-stats")
    public Response teamStatsByNativeQuery() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT
              t.id AS team_id,
              t.name AS team_name,
              COUNT(m.id) AS manager_count
            FROM team t
            LEFT JOIN manager m
              ON m.team_id = t.id
             AND m.is_active = true
            WHERE t.is_active = true
            GROUP BY t.id, t.name
            ORDER BY t.id
            """).getResultList();

        List<TeamManagerCount> items = rows.stream()
            .map(row -> new TeamManagerCount(
                ((Number) row[0]).intValue(),
                (String) row[1],
                ((Number) row[2]).longValue()
            ))
            .toList();

        return Response.ok(items).build();
    }

    @GET
    @Path("/teams/native-summary")
    public Response teamSummaryByNativeQuery() {
        Object[] row = (Object[]) entityManager.createNativeQuery("""
            SELECT
              COUNT(*) AS total_teams,
              COUNT(*) FILTER (WHERE is_active = true) AS active_teams,
              MAX(id) AS max_team_id
            FROM team
            """).getSingleResult();

        TeamNativeSummary summary = new TeamNativeSummary(
            ((Number) row[0]).longValue(),
            ((Number) row[1]).longValue(),
            row[2] == null ? null : ((Number) row[2]).intValue()
        );

        return Response.ok(summary).build();
    }

    @GET
    @Path("/teams/native-cte-stats")
    public Response teamStatsByNativeCte(
        @QueryParam("min_manager_count") @DefaultValue("0") int minManagerCount
    ) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
            WITH active_manager_counts AS (
              SELECT
                t.id AS team_id,
                t.name AS team_name,
                COUNT(m.id) AS manager_count
              FROM team t
              LEFT JOIN manager m
                ON m.team_id = t.id
               AND m.is_active = true
              WHERE t.is_active = true
              GROUP BY t.id, t.name
            )
            SELECT
              team_id,
              team_name,
              manager_count,
              DENSE_RANK() OVER (ORDER BY manager_count DESC, team_id ASC) AS manager_rank
            FROM active_manager_counts
            WHERE manager_count >= :minManagerCount
            ORDER BY manager_rank, team_id
            """)
            .setParameter("minManagerCount", minManagerCount)
            .getResultList();

        List<TeamManagerCteStat> items = rows.stream()
            .map(row -> new TeamManagerCteStat(
                ((Number) row[0]).intValue(),
                (String) row[1],
                ((Number) row[2]).longValue(),
                ((Number) row[3]).longValue()
            ))
            .toList();

        return Response.ok(items).build();
    }

    public record TeamPageResponse(
        int page,
        int size,
        long total,
        List<Team> items
    ) {
    }

    public record TeamManagerCount(
        int teamId,
        String teamName,
        long managerCount
    ) {
    }

    public record TeamNativeSummary(
        long totalTeams,
        long activeTeams,
        Integer maxTeamId
    ) {
    }

    public record TeamManagerCteStat(
        int teamId,
        String teamName,
        long managerCount,
        long managerRank
    ) {
    }
}
