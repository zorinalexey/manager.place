package lidofon.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "int_id", nullable = false, unique = true)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "int_id_generator")
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@SequenceGenerator(name = "int_id_generator", sequenceName = "your_sequence_name", allocationSize = 1)
    private Long intId;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "middle_name",nullable = false)
    private String middleName;
    @Column(name = "last_name",nullable = false)
    private String lastName;
    @Column(name = "email",unique = true,nullable = false)
    private String email;
    @Column(name = "login",unique = true,nullable = false)
    private String login;
    @Column(name = "date_of_birth",nullable = false)
    private LocalDate dateOfBirth;
    @Column(name = "virtual_number",nullable = false)
    private String virtualNumber;
    @Column(name = "phone",nullable = false)
    private String phone;
    @Column(name = "operator_group_id",nullable = false)
    private String operatorGroupId;
    @Column(name = "external_id",nullable = false)
    private String externalId;
    @Column(name = "add_external_number",nullable = false)
    private String addExternalNumber;
    @Column(name="role_id",nullable = false)
    private String roleId;
    @Column(name="city_id",nullable = false)
    private String cityId;
    @Column(name="region_id",nullable = false)
    private String regionId;
    @Column(name="country_id",nullable = false)
    private String countryId;
    @Column(name="is_blocked",nullable = false)
    private Boolean isBlocked;
    @Column(name="status",nullable = false)
    private String status;
    @Column(name="password",nullable = false)
    private String password;
}
