package nigglenandu.foodigo.foodigo.Security.SecurityConfig;

import nigglenandu.foodigo.foodigo.Security.Repository.RoleRepository;
import nigglenandu.foodigo.foodigo.model.RoleEntity;
import nigglenandu.foodigo.foodigo.model.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Roles> roles = Arrays.asList(Roles.ADMIN, Roles.CUSTOMER, Roles.DELIVERY_PERSON, Roles.KITCHEN_MANAGER);

        for(Roles role : roles){
            if(!roleRepository.findByRole(role).isPresent()){
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRole(role);
                roleRepository.save(roleEntity);
            }
        }
    }
}
