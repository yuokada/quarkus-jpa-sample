package io.github.yuokada.npb;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TeamRepository implements PanacheRepositoryBase<Team, Integer> {

    public Team findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Team> allTeam(Boolean includeDeleted) {
        if (includeDeleted != null && includeDeleted) {
            return listAll(Sort.by("id"));
        } else {
            return find("is_active", true).list();
        }
    }
}
