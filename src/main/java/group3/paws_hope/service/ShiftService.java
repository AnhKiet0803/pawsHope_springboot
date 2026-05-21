package group3.paws_hope.service;

import group3.paws_hope.dto.req.ShiftReq;
import group3.paws_hope.dto.res.ShiftRes;
import group3.paws_hope.entity.Shift;
import group3.paws_hope.repository.ShiftRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;

    public List<ShiftRes> getAllShifts() {
        return shiftRepository.findAll().stream()
                .map(ShiftRes::toJison)
                .toList();
    }

    public ShiftRes findById(Long id) {
        return ShiftRes.
        toJison(shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found")));
    }

    public ShiftRes create(ShiftReq req) {
        try {
            Shift shift = new Shift();
            shift.setShiftName(req.getShiftName());
            shift.setStartTime(req.getStartTime());
            shift.setEndTime(req.getEndTime());
            shift.setCrossesMidnight(req.getCrossesMidnight());
            return ShiftRes.toJison(shiftRepository.save(shift));
        } catch (Exception e) {
            return null;
        }
    }

    public ShiftRes update(Long id, ShiftReq req) {
        try {
            Shift shift = shiftRepository.findById(id).orElseThrow();
            shift.setShiftName(req.getShiftName());
            shift.setStartTime(req.getStartTime());
            shift.setEndTime(req.getEndTime());
            shift.setCrossesMidnight(req.getCrossesMidnight());
            return ShiftRes.toJison(shiftRepository.save(shift));
        } catch (Exception e) {
            return null;
        }
    }

}