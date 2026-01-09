package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/member-points")
@CrossOrigin(origins = "*")
public class MemberPointsController {

    @Autowired
    private MemberGamificationService memberGamificationService;

    // GET - Consultar pontos de um membro
    @GetMapping("/member/{memberId}")
    public ResponseEntity<MemberPoints> getMemberPoints(@PathVariable String memberId) {
        try {
            Optional<MemberPoints> memberPoints = memberGamificationService.getMemberPoints(memberId);
            if (memberPoints.isPresent()) {
                return ResponseEntity.ok(memberPoints.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar ranking de membros por pontos
    @GetMapping("/leaderboard")
    public ResponseEntity<List<MemberPoints>> getLeaderboard() {
        try {
            List<MemberPoints> leaderboard = memberGamificationService.getTopMembersByPoints();
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar membros com pontos mínimos
    @GetMapping("/minimum-points/{minPoints}")
    public ResponseEntity<List<MemberPoints>> getMembersByMinimumPoints(@PathVariable long minPoints) {
        try {
            List<MemberPoints> members = memberGamificationService.getTopMembersByPoints().stream()
                    .filter(mp -> mp.getTotalPoints() >= minPoints)
                    .toList();
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Adicionar pontos a um membro
    @PostMapping("/member/{memberId}/add-points")
    public ResponseEntity<MemberPoints> addPointsToMember(@PathVariable String memberId, @RequestBody PointsRequest request) {
        try {
            memberGamificationService.addPointsToMember(memberId, request.getPoints());

            // Retorna os pontos atualizados do membro
            Optional<MemberPoints> updatedPoints = memberGamificationService.getMemberPoints(memberId);
            if (updatedPoints.isPresent()) {
                return ResponseEntity.ok(updatedPoints.get());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar pontos de um membro
    @PutMapping("/member/{memberId}")
    public ResponseEntity<MemberPoints> updateMemberPoints(@PathVariable String memberId, @RequestBody PointsUpdateRequest request) {
        try {
            Optional<MemberPoints> optionalMemberPoints = memberGamificationService.getMemberPoints(memberId);
            if (optionalMemberPoints.isPresent()) {
                MemberPoints memberPoints = optionalMemberPoints.get();
                memberPoints.setTotalPoints(request.getTotalPoints());

                // Salvar através do repository (preciso adicionar método no service)
                return ResponseEntity.ok(memberPoints);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class PointsRequest {
        private long points;

        // Getters e Setters
        public long getPoints() { return points; }
        public void setPoints(long points) { this.points = points; }
    }

    public static class PointsUpdateRequest {
        private long totalPoints;

        // Getters e Setters
        public long getTotalPoints() { return totalPoints; }
        public void setTotalPoints(long totalPoints) { this.totalPoints = totalPoints; }
    }
}
