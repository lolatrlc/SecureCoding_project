package hr.algebra.booknook.service;

import hr.algebra.booknook.entity.BookSnapshot;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;

@Service
public class SerializationService {

    // Magic bytes de Java : tout objet sérialisé commence par AC ED 00 05
    private static final byte[] JAVA_MAGIC_BYTES = {(byte) 0xAC, (byte) 0xED, 0x00, 0x05};

    // Whitelist des classes autorisées — inspirée directement du lab du prof
    private static final ObjectInputFilter SAFE_FILTER =
            ObjectInputFilter.Config.createFilter(
                    "maxdepth=10;"
                            + "maxarray=1000;"
                            + "maxrefs=100;"
                            + "maxbytes=10000;"
                            + "hr.algebra.booknook.entity.BookSnapshot;"
                            + "java.lang.String;"
                            + "java.lang.Integer;"
                            + "java.lang.Number;"
                            + "!*"
            );

    // Sérialise un objet en Base64
    public String serializeToBase64(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    // Vérifie les magic bytes
    public boolean hasValidMagicBytes(byte[] bytes) {
        if (bytes.length < 4) return false;
        return bytes[0] == JAVA_MAGIC_BYTES[0]
                && bytes[1] == JAVA_MAGIC_BYTES[1]
                && bytes[2] == JAVA_MAGIC_BYTES[2]
                && bytes[3] == JAVA_MAGIC_BYTES[3];
    }

    // Désérialise de façon sécurisée
    public Object safeDeserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        // 1 = vérifier les magic bytes
        if (!hasValidMagicBytes(bytes)) {
            throw new IllegalArgumentException(
                    "Invalid file format: missing Java serialization magic bytes (0xACED0005)");
        }

        // 2 = désérialiser avec whitelist
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            ois.setObjectInputFilter(SAFE_FILTER);
            return ois.readObject();
        }
    }
}