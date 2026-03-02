package io.github.yuokada.npb;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PlayerTransferHistoryRepository implements PanacheRepositoryBase<PlayerTransferHistory, Integer> {

    public List<PlayerTransferHistory> findByPlayerIdOrderByTransferredAtAsc(Integer playerId) {
        return find("player.id = ?1 order by transferredAt asc, id asc", playerId).list();
    }
}
