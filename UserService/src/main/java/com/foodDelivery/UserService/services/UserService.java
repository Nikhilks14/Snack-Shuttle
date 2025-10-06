package com.foodDelivery.UserService.services;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public AppUser getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public List<AppUser> listAll() {
        return userRepository.findAll();
    }

    public AppUser updateProfile(Long userId, UpdateUserDto dto) {
        AppUser u = getById(userId);
        if (dto.getName() != null) u.setName(dto.getName());
        if (dto.getPhone() != null) u.setPhone(dto.getPhone());
        return userRepository.save(u);
    }


    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    // Admin only
    public AppUser changeRole(Long userId, com.example.userservice.model.Role role) {
        AppUser u = getById(userId);
        u.setRole(role);
        return userRepository.save(u);
    }
}