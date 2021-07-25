package com.jalin.jalinappbackend.module.gamification.point.presenter.controller;

import com.jalin.jalinappbackend.model.SuccessResponse;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDetailDto;
import com.jalin.jalinappbackend.module.gamification.point.model.PointDto;
import com.jalin.jalinappbackend.module.gamification.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${url.map.api}/v1")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping("/points")
    public ResponseEntity<Object> getUserPoint() {
        PointDto pointDto = pointService.getUserPoint();
        return new ResponseEntity<>(pointDto, HttpStatus.OK);
    }

    @GetMapping("/points/details")
    public ResponseEntity<Object> getUserPointDetails() {
        List<PointDetailDto> pointDetailDtoList = pointService.getUserPointDetails();
        return new ResponseEntity<>(pointDetailDtoList, HttpStatus.OK);
    }

    @PostMapping("/points/addPoints")
    public ResponseEntity<Object> addUserPointQa(@RequestParam(name = "amount") Integer amount) {
        pointService.addUserPointQa(amount);
        return new ResponseEntity<>(
                new SuccessResponse(true,"duh kamu ini kebiasaan ngetest sana sini ya, nakal deh. " +
                        "Oh iya ini pointnya udah ditambahin jadi " + amount + " nih 💋"),
                HttpStatus.OK
        );
    }

    @PostMapping("/points/resetPoints")
    public ResponseEntity<Object> resetUserPoints() {
        pointService.resetUserPointQa();
        return new ResponseEntity<>(
                new SuccessResponse(true,"ih kok jahat banget point orang di reset :(." +
                        " Udah aku reset jadi 0 nih point-nya 😒"),
                HttpStatus.OK
        );
    }
}
