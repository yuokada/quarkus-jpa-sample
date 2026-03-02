package io.github.yuokada.npb;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ManagerRepository implements PanacheRepositoryBase<Manager, Integer> {
    public Manager findByName(String name) {
        return find("name", name).firstResult();
    }

    public List<Manager> listByIncludeDeleted(Boolean includeDeleted) {
        if (includeDeleted != null && includeDeleted) {
            return listAll(Sort.by("id"));
        } else {
            return find("isActive", true).list();
        }
    }
}
