package com.szymontracz.warehouse.service;


import com.szymontracz.warehouse.entity.UserEntity;
import com.szymontracz.warehouse.model.UserDto;
import com.szymontracz.warehouse.repository.UserRepository;
import com.szymontracz.warehouse.security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

//    FileStore fileStore;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRepository userRepository;
    JwtUtil jwtUtil;
    RestTemplate restTemplate;
    Environment environment;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, JwtUtil jwtUtil, RestTemplate restTemplate) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;

    }

    @Override
    public UserDto getUserById(UUID id) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String email) {
        return null;
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userEntity.setUserId(UUID.randomUUID());
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        System.out.println(userEntity.getEncryptedPassword() + "PASSWORD");
        System.out.println(userEntity.getUserId() + "ID");
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, // Email verification status
                true, true, true, new ArrayList<>());
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


//    @Override
//    public Optional<UserDto> getUserById(UUID id) {
//        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findUserById(id).orElse(null)));
//    }
//
//    @Override
//    public Optional<UserDto> getUserByEmail(String email) {
//        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findByEmail(email).orElse(null)));
//    }
//
//    @Override
//    public Optional<UserDto> getUserByUsername(String username) {
//        return Optional.ofNullable(userMapper.userToUserDto(userRepository.findByUsername(username).orElse(null)));
//    }
//
//    @Override
//    public UserDto saveNewUser(UserDto userDto) {
//        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
//    }
//
//    @Override
//    public Optional<UserDto> updateUserById(UUID uuid, UserDto userDto) {
//        return Optional.empty();
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
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user;
//        if (userRepository.findByUsername(username).isPresent()) {
//            user = userRepository.findByUsername(username).get();
//        } else {
//            throw new UsernameNotFoundException(username);
//        }
//        return new User();
//    }
}
