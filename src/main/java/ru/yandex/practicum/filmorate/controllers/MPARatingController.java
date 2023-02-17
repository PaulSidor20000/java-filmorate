package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.mparating.MPARatingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MPARatingController {
    private final MPARatingService service;

    @GetMapping
    public List<MPARating> findAllRatings() {
        return service.findAllRatings();
    }

    @PostMapping
    public MPARating addRating(@Valid @RequestBody MPARating mpaRating) {
        return service.addRating(mpaRating);
    }

    @PutMapping
    public MPARating updateRating(@Valid @RequestBody MPARating mpaRating) {
        return service.updateRating(mpaRating);
    }

    @GetMapping("/{id}")
    public MPARating findRatingById(@PathVariable(value = "id") Integer mpaRatingId) {
        return service.findRatingById(mpaRatingId);

    }

}
