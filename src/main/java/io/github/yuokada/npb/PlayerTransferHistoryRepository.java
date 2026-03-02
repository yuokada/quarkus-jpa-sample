package io.github.yuokada.npb;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlayerTransferHistoryRepository implements PanacheRepositoryBase<PlayerTransferHistory, Integer> {

    public List<PlayerTransferHistory> findByPlayerIdOrderByTransferredAtAsc(Integer playerId) {
        return find("player.id = ?1 order by transferredAt asc, id asc", playerId).list();
    }

    public List<PlayerTransferHistory> search(Integer playerId, LocalDateTime from, LocalDateTime to, int limit) {
        StringBuilder query = new StringBuilder("player.id = ?1");
        List<Object> params = new ArrayList<>();
        params.add(playerId);

        if (from != null) {
            query.append(" and transferredAt >= ?").append(params.size() + 1);
            params.add(from);
        }

        if (to != null) {
            query.append(" and transferredAt <= ?").append(params.size() + 1);
            params.add(to);
        }

        query.append(" order by transferredAt asc, id asc");
        return find(query.toString(), params.toArray())
            .page(Page.ofSize(limit))
            .list();
    }
}
