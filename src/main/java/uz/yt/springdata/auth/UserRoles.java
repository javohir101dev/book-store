package uz.yt.springdata.auth;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uz.yt.springdata.dao.Authorities;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uz.yt.springdata.auth.UserPermissions.*;

@Getter
public enum UserRoles {
    GUEST(Stream.of(READ_BOOK, READ_DELIVERY, CREATE_ORDER_ITEMS, READ_ORDERS).collect(Collectors.toSet()), "GUEST", 12),
    ADMIN(Stream.of(CREATE, UPDATE, READ, DELETE).collect(Collectors.toSet()), "ADMIN", 13),
    MODERATOR(Stream.of(CREATE, UPDATE, READ).collect(Collectors.toSet()), "MODERATOR", 14),
    BOOK_MANAGER(Stream.of(CREATE_BOOK, READ).collect(Collectors.toSet()), "BOOK_MANAGER", 15),
    SALES_MANAGER(Stream.of(CREATE_ORDERS, READ, CREATE_DELIVERY).collect(Collectors.toSet()), "SALES_MANAGER", 16);

    private final Set<UserPermissions> permissions;
    private final String name;
    private Integer id;

    UserRoles(Set<UserPermissions> permissions, String name, Integer id) {
        this.permissions = permissions;
        this.name = name;
        this.id = id;
    }

    public Set<SimpleGrantedAuthority> getPermissions() {
        Set<SimpleGrantedAuthority> authorities =
                permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));

        return authorities;
    }

    public Set<Authorities> getAuthorities(){
        Set<Authorities> authorities =  permissions.stream()
                .map(p -> new Authorities(p.getId(), p.getName()))
                .collect(Collectors.toSet());
        authorities.add(new Authorities(getId(), "ROLE_" + getName()));
        return authorities;
    }

    public String getName(){
        return name;
    }
}
