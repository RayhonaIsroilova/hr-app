package ecma.ai.hrapp.entity.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum RoleName {
    ROLE_DIRECTOR(4),
    ROLE_MANAGER(3),//bu HR_MANAGER
    ROLE_STAFF(2),
    ROLE_USE(1);


    private int count;
        public int getRoleC() {
        return this.count;
    }
}
