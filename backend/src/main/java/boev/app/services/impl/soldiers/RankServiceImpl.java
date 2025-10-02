package boev.app.services.impl.soldiers;

import boev.app.error.exception.Error404;
import boev.app.error.exception.Error500;
import boev.app.models.ModelMapper;
import boev.app.models.soldiers.Rank;
import boev.app.payload.soldiers.rank.RankDto;
import boev.app.payload.soldiers.rank.RankResponse;
import boev.app.repository.soliders.RankRepository;
import boev.app.services.interfaces.soldiers.RankService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RankServiceImpl implements RankService {
    private final RankRepository rankRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public List<RankResponse> getAll() {
        return rankRepository.findAll().stream().map(modelMapper::toRankResponse).collect(Collectors.toList());
    }

    @Transactional
    public RankResponse get(long id) {
        return modelMapper.toRankResponse(rankRepository.findById(id).orElseThrow(() -> new Error404("Rank not found")));
    }

    @Transactional
    public RankResponse delete(long id) {
        RankResponse rankResponse = modelMapper.toRankResponse(rankRepository.findById(id).orElseThrow(() -> new Error404("Rank not found")));
        rankRepository.deleteById(id);
        return rankResponse;
    }

    @Transactional
    public RankResponse update(long id, RankDto rankDto) {
        Rank rank = rankRepository.findById(id).orElseThrow(() -> new Error404("Rank not found"));
        rank.setCategory(rankDto.getCategory());
        rank.setName(rankDto.getName());

        try{
            rank = rankRepository.save(rank);
            return modelMapper.toRankResponse(rank);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }

    }

    @Transactional
    public RankResponse create(RankDto rankDto) {
        Rank rank = modelMapper.toRank(rankDto);

        try{
            rank = rankRepository.save(rank);
            return modelMapper.toRankResponse(rank);
        }catch (DataIntegrityViolationException e) {
            throw new Error500();
        }
    }
}
