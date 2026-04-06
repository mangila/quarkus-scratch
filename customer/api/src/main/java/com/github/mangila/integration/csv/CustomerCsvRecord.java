package com.github.mangila.integration.csv;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",", skipFirstLine = true)
public class CustomerCsvRecord {

    public static final String CSV_HEADERS = "id,name,address,email,phone";

    @DataField(pos = 1)
    @NotNull
    private String id;

    @DataField(pos = 2)
    @NotBlank
    private String name;

    @DataField(pos = 3)
    @NotBlank
    private String address;

    @DataField(pos = 4)
    @Email
    private String email;

    @DataField(pos = 5)
    @NotBlank
    private String phone;

    public CustomerCsvRecord() {
    }

    public CustomerCsvRecord(String id, String name, String address, String email, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
