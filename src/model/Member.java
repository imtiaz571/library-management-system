package model;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private int memberId;
    private String name;
    private String email;
    private MembershipType type;

    public Member(int memberId, String name, String email, MembershipType type) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.type = type;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MembershipType getType() {
        return type;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
