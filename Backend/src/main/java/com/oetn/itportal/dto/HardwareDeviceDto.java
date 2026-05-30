package com.oetn.itportal.dto;

import java.time.LocalDateTime;

public class HardwareDeviceDto {

    public static class CreateRequest {
        private String laptopModel;
        private String serialNumber;

        public String getLaptopModel() {
            return laptopModel;
        }

        public void setLaptopModel(String laptopModel) {
            this.laptopModel = laptopModel;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }
    }

    public static class Response {
        private Long id;
        private String username, laptopModel, serialNumber, userEmail;
        private LocalDateTime submittedAt;

        public Response() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String u) {
            this.username = u;
        }

        public String getLaptopModel() {
            return laptopModel;
        }

        public void setLaptopModel(String m) {
            this.laptopModel = m;
        }

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String s) {
            this.serialNumber = s;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String e) {
            this.userEmail = e;
        }

        public LocalDateTime getSubmittedAt() {
            return submittedAt;
        }

        public void setSubmittedAt(LocalDateTime s) {
            this.submittedAt = s;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Long id;
            private String username, laptopModel, serialNumber, userEmail;
            private LocalDateTime submittedAt;

            public Builder id(Long id) {
                this.id = id;
                return this;
            }

            public Builder username(String u) {
                this.username = u;
                return this;
            }

            public Builder laptopModel(String m) {
                this.laptopModel = m;
                return this;
            }

            public Builder serialNumber(String s) {
                this.serialNumber = s;
                return this;
            }

            public Builder userEmail(String e) {
                this.userEmail = e;
                return this;
            }

            public Builder submittedAt(LocalDateTime s) {
                this.submittedAt = s;
                return this;
            }

            public Response build() {
                Response r = new Response();
                r.id = id;
                r.username = username;
                r.laptopModel = laptopModel;
                r.serialNumber = serialNumber;
                r.userEmail = userEmail;
                r.submittedAt = submittedAt;
                return r;
            }
        }
    }
}
