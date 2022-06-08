package uz.yt.springdata.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static uz.yt.springdata.auth.UserPermissions.*;

public enum UserRoles {
    GUEST(Set.of(READ_BOOK, READ_DELIVERY, CREATE_ORDER_ITEMS, READ_ORDERS), "GUEST", 12),
    ADMIN(Set.of(CREATE, UPDATE, READ, DELETE), "ADMIN", 13),
    MODERATOR(Set.of(CREATE, UPDATE, READ), "MODERATOR", 14),
    BOOK_MANAGER(Set.of(CREATE_BOOK, READ), "BOOK_MANAGER", 15),
    SALES_MANAGER(Set.of(CREATE_ORDERS, READ, CREATE_DELIVERY), "SALES_MANAGER", 16);

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

    public String getName(){
        return name;
    }
}
