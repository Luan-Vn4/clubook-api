    package br.upe.booklubapi.infra.core.gateways.mediastorage;

import br.upe.booklubapi.domain.core.gateways.mediastorage.MediaStorageGateway;
import br.upe.booklubapi.domain.core.gateways.mediastorage.exceptions.BucketAlreadyExistsException;
import br.upe.booklubapi.domain.core.gateways.mediastorage.exceptions.MediaStorageException;
import br.upe.booklubapi.utils.UnitUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Primary
public class S3MediaStorageGateway implements MediaStorageGateway {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    @Override
    public void createBucket(String bucket) {
        if (bucketExists(bucket)) {
            throw new BucketAlreadyExistsException(bucket);
        }
        requestBucketCreation(bucket);
    }

    private void requestBucketCreation(String bucket) {
        try {
            s3Client.createBucket(builder -> builder
                .bucket(bucket)
                .build()
            );
        } catch (S3Exception s3Exception) {
            throw new MediaStorageException(s3Exception);
        }
    }

    @Override
    public boolean createBucketIfNotExists(String bucket) {
        if (bucketExists(bucket)) return false;
        requestBucketCreation(bucket);
        return true;
    }

    @Override
    public boolean bucketExists(String bucket) {
        try {
            s3Client.headBucket(builder -> builder
                .bucket(bucket)
                .build());
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw new MediaStorageException(e);
        }
    }

    @Override
    public boolean objectExists(String bucket, String object) {
        if (!StringUtils.hasText(object)) {
            return false;
        }
        try {
            s3Client.headObject(builder -> builder
                .bucket(bucket)
                .key(object)
                .build()
            );
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) return false;
            throw new MediaStorageException(e);
        }
    }

    @Override
    public String uploadObject(String bucket, String object, MultipartFile file) {
        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(object)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return object;
        } catch (IOException | S3Exception e) {
            throw new MediaStorageException(e);
        }
    }

    @Override
    public Optional<String> getObjectUrl(
        String bucket,
        String object,
        int expirationTime,
        TimeUnit timeUnit
    ) {
        if (!StringUtils.hasText(object)
            || !bucketExists(bucket)
            || !objectExists(bucket, object)) {
            return Optional.empty();
        }

        try {
            final var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(object)
                .build();

            final Duration duration = UnitUtils
                .getDuration(expirationTime, timeUnit);

            final var presignedRequest = s3Presigner.presignGetObject(
                builder -> builder
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(duration)
                    .build()
            );

            return Optional.of(presignedRequest.url().toString());
        } catch (S3Exception e) {
            throw new MediaStorageException(e);
        }
    }

    @Override
    public void deleteObject(String bucket, String object) {
        try {
            s3Client.deleteObject(builder -> builder
                .bucket(bucket)
                .key(object)
            );
        } catch (S3Exception e) {
            throw new MediaStorageException(e);
        }
    }

}
