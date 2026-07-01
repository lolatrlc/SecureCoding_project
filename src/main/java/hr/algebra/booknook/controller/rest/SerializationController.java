package hr.algebra.booknook.controller.rest;

import hr.algebra.booknook.entity.BadThing;
import hr.algebra.booknook.entity.BookSnapshot;
import hr.algebra.booknook.service.SerializationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/serialization")
@Tag(name = "Serialization", description = "Serialization and deserialization demo")
public class SerializationController {

    private final SerializationService serializationService;

    public SerializationController(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    @GetMapping("/serialize")
    public ResponseEntity<Map<String, String>> serialize() {
        try {
            BookSnapshot snapshot = new BookSnapshot(
                    "The Seven Husbands of Evelyn Hugo",
                    "Taylor Jenkins Reid",
                    "FINISHED",
                    5
            );

            String base64 = serializationService.serializeToBase64(snapshot);
            byte[] bytes = Base64.getDecoder().decode(base64);

            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < Math.min(8, bytes.length); i++) {
                hex.append(String.format("%02X ", bytes[i]));
            }

            Map<String, String> result = new LinkedHashMap<>();
            result.put("object", snapshot.toString());
            result.put("base64", base64.substring(0, Math.min(60, base64.length())) + "...");
            result.put("fullBase64", base64);
            result.put("magicBytes", hex.toString().trim());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/deserialize-safe")
    public ResponseEntity<Map<String, String>> deserializeSafe(@RequestBody Map<String, String> body) {
        try {
            String base64 = body.get("base64");
            byte[] bytes = Base64.getDecoder().decode(base64);

            Object result = serializationService.safeDeserialize(bytes);

            Map<String, String> response = new LinkedHashMap<>();
            if (result instanceof BookSnapshot snapshot) {
                response.put("status", "Accepted - whitelisted class");
                response.put("type", result.getClass().getName());
                response.put("object", snapshot.toString());
            } else {
                response.put("status", "Rejected - unknown class");
                response.put("type", result.getClass().getName());
            }
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status", "Rejected - invalid magic bytes");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status", "Rejected - " + e.getClass().getSimpleName());
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/serialize-badthing")
    public ResponseEntity<Map<String, String>> serializeBadThing() {
        try {
            BadThing badThing = new BadThing();
            badThing.setLooselyDefinedThing("target");
            badThing.setMethodName("toString");

            String base64 = serializationService.serializeToBase64(badThing);

            Map<String, String> result = new LinkedHashMap<>();
            result.put("base64", base64.substring(0, Math.min(60, base64.length())) + "...");
            result.put("fullBase64", base64);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}