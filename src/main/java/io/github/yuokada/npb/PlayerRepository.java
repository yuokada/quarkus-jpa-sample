package io.github.yuokada.npb;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlayerRepository implements PanacheRepositoryBase<Player, Integer> {

    public List<Player> search(Boolean includeDeleted, Integer teamId, String position) {
        StringBuilder query = new StringBuilder();
        List<Object> params = new ArrayList<>();

        if (includeDeleted == null || !includeDeleted) {
            query.append("isActive = ?").append(params.size() + 1);
            params.add(true);
        }

        if (teamId != null) {
            if (!query.isEmpty()) {
                query.append(" and ");
            }
            query.append("team.id = ?").append(params.size() + 1);
            params.add(teamId);
        }

        if (position != null && !position.isBlank()) {
            if (!query.isEmpty()) {
                query.append(" and ");
            }
            query.append("position = ?").append(params.size() + 1);
            params.add(position.trim().toUpperCase());
        }

        if (query.isEmpty()) {
            return listAll(Sort.by("id"));
        }
        return find(query.toString(), Sort.by("id"), params.toArray()).list();
    }
}
