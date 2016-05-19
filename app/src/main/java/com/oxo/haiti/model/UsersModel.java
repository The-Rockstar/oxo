package com.oxo.haiti.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaswinderwadali on 5/17/2016.
 */
public class UsersModel  implements Serializable{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("users")
    @Expose
    private List<User> users = new ArrayList<>();

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @param users The users
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }


    public static class User  implements Serializable {
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;

        /**
         * @return The username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username The username
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return The password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password The password
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }


}
