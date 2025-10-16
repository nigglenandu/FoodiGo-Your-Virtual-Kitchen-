package nigglenandu.foodigo.foodigo.Chef;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ChefEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chefId;

    private String fname;
    private String lname;
    private String email;
    private String mobile;
    private String password;
    private String confirmPassword;

    private String house;
    private String area;
    private String city;
    private String suburban;
    private String state;
    private String postcode;

    @OneToMany(mappedBy = "chef", cascade = CascadeType.ALL)
    private List<ChefFinalOrder> orders;

    public ChefEntity() {}

    public ChefEntity(String fname, String lname, String email, String mobile, String password, String confirmPassword,
                String house, String area, String city, String suburban, String state, String postcode) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.house = house;
        this.area = area;
        this.city = city;
        this.suburban = suburban;
        this.state = state;
        this.postcode = postcode;
    }
}