package boev.app.services.interfaces.soldiers;

import boev.app.payload.soldiers.rank.RankDto;
import boev.app.payload.soldiers.rank.RankResponse;

import java.util.List;

public interface RankService {
    List<RankResponse> getAll();
    RankResponse get(long id);
    RankResponse delete(long id);
    RankResponse update(long id, RankDto rankDto);
    RankResponse create(RankDto rankDto);
}
