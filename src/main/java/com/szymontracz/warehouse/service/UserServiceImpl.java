package com.szymontracz.warehouse.service;


import com.szymontracz.warehouse.amazon.filestore.FileStore;
import com.szymontracz.warehouse.auth.MessageResponse;
import com.szymontracz.warehouse.auth.Request;
import com.szymontracz.warehouse.security.jwt.JwtUtils;
import com.szymontracz.warehouse.entity.EmailToken;
import com.szymontracz.warehouse.entity.Role;
import com.szymontracz.warehouse.entity.User;
import com.szymontracz.warehouse.dto.UserDto;
import com.szymontracz.warehouse.repository.TokenRepository;
import com.szymontracz.warehouse.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${confirmation.url}")
    private String CONFIRMATION_URL;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    FileStore fileStore;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, TokenRepository tokenRepository, EmailService emailService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public UserDto getUserById(UUID id) {
        return null;
    }

//    @Override
//    public UserDto saveNewUser(UserDto userDto) {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        User user = modelMapper.map(userDto, User.class);
//        userRepository.save(user);
//        return modelMapper.map(user, UserDto.class);
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow( () -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public ResponseEntity<MessageResponse> registerNewUser(Request registrationDto) {
        if (isUserExists(registrationDto)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        User newUser = createNewUser(registrationDto);
        userRepository.save(newUser);

        EmailToken newEmailToken = createNewToken(newUser);
        tokenRepository.save(newEmailToken);

        sendEmail(registrationDto, newEmailToken);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private boolean isUserExists(Request registrationDto) {
        return userRepository.findByEmail(registrationDto.getEmail()).isPresent();
    }

    private User createNewUser(Request registrationDto) {
        return User.builder()
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Role.USER)
                .build();
    }

    private EmailToken createNewToken(User newUser) {
        return EmailToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(newUser)
                .build();
    }

    private void sendEmail(Request registrationDto, EmailToken newEmailToken) {
        try {
            emailService.send(
                    registrationDto.getEmail(),
                    null,
                    String.format(CONFIRMATION_URL, newEmailToken.getToken())
            );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public String confirm(String token) {
        EmailToken savedToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new IllegalStateException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            String generatedToken = UUID.randomUUID().toString();
            EmailToken newToken = EmailToken.builder()
                    .token(generatedToken)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(10))
                    .user(savedToken.getUser())
                    .build();
            tokenRepository.save(newToken);

            try {
                emailService.send(
                        savedToken.getUser().getEmail(),
                        null,
                        String.format(CONFIRMATION_URL, generatedToken)
                );
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "Token expired, a new token has been sent to your email";
        }

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);

        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());

        tokenRepository.save(savedToken);
        return "<h1>Your account has been successfully activated</h1>";
    }

    @Override
    public ResponseCookie authenticate(Request loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        return jwtUtils.generateJwtCookie(user.getEmail());
    }

    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie();
    }















//    @Override
//    public ResponseEntity<MessageResponse> uploadProfilePictureValidation(UUID userProfileId, MultipartFile file) {
//        if (isFileNotEmpty(file) && isFileAnImage(file) &&
//                userRepository.findUserById(userProfileId).isPresent()) {
//            User user = userRepository.findUserById(userProfileId).get();
//            Map<String, String> metadata = extractMetadata(file);
//            uploadProfilePicture(user, file, metadata);
//            return ResponseEntity.ok(new MessageResponse("Image uploaded successfully."));
//        }
//        return ResponseEntity.badRequest().body(new MessageResponse("There's been an error, please try again."));
//    }

//    private Map<String, String> extractMetadata(MultipartFile file) {
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("Content-Type", file.getContentType());
//        metadata.put("Content-Length", String.valueOf(file.getSize()));
//        return metadata;
//    }
//
//    private void uploadProfilePicture(User user, MultipartFile file, Map<String, String> metadata) {
//        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
//        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
//        try {
//            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
//            user.setUserProfileImageLink(filename);
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }
//    }

//    @Override
//    public byte[] downloadUserProfileImage(UUID userProfileId) {
//        if (userRepository.findUserById(userProfileId).isPresent()) {
//            User user = userRepository.findUserById(userProfileId).get();
//            String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getId());
//            if (user.getUserProfileImageLink() != null) {
//                return fileStore.download(path, user.getUserProfileImageLink());
//            }
//        }
//        return new byte[0];
//    }
//
//    @Override
//    public Boolean deleteUserById(UUID userId) {
//        if (userRepository.existsById(userId)) {
//            userRepository.deleteById(userId);
//            return true;
//        }
//        return false;
//    }
//
//    private boolean isFileNotEmpty(MultipartFile file) {
//        if (file.isEmpty()) {
//            throw new IllegalStateException("File is empty! [ " + file.getSize() + " ]");
//        } else {
//            return true;
//        }
//    }
//
//    private boolean isFileAnImage(MultipartFile file) {
//        if (!Arrays.asList(
//                        ContentType.IMAGE_JPEG.getMimeType(),
//                        ContentType.IMAGE_PNG.getMimeType(),
//                        ContentType.IMAGE_GIF.getMimeType())
//                .contains(file.getContentType())) {
//            throw new IllegalStateException("File must be an image! [ " + file.getContentType() + " ]");
//        } else {
//            return true;
//        }
//    }
//

}
