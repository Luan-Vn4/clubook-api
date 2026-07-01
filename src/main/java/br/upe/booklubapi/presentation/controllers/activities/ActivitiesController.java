package br.upe.booklubapi.presentation.controllers.activities;

import br.upe.booklubapi.app.activities.dtos.ActivityDTO;
import br.upe.booklubapi.app.activities.services.ActivitiesService;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.utils.docs.ApiTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/activities")
@AllArgsConstructor
@Tag(name=ApiTag.ACTIVITIES)
public class ActivitiesController {

    private final ActivitiesService activitiesService;

    @GetMapping
    @Operation(summary="Get relevant activities for the logged user")
    public ResponseEntity<PagedModel<ActivityDTO>> getActivities(
        Pageable pageable,
        @RequestParam(name="type", required=false)
        Optional<List<ActivityType>> type
    ) {
        return ResponseEntity.ok(activitiesService.getActivitiesForUser(pageable, type));
    }

}
