package nextstep.subway.line.application;

import nextstep.subway.exception.DuplicateNameException;
import nextstep.subway.exception.NoSuchDataException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> searchLineAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse searchLine(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 노선 ID입니다."));
        return LineResponse.of(line);
    }

    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new NoSuchDataException("존재하지 않는 노선 ID입니다."));
        line.update(lineRequest.toLine());
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional(readOnly = true)
    public void validateCheck(LineRequest lineRequest) {
        if (lineRepository.existsByName(lineRequest.getName())) {
            throw new DuplicateNameException("이미 존재하는 노선 이름입니다.");
        }
    }

}
