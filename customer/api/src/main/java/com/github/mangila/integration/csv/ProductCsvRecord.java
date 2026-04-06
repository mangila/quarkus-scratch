package com.github.mangila.integration.csv;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.hibernate.validator.constraints.URL;

@CsvRecord(separator = ",", skipFirstLine = true, generateHeaderColumns = true)
public class ProductCsvRecord {

    public static final String CSV_HEADERS = "id,name,image_url,price";

    @DataField(pos = 1)
    @NotNull
    private String id;

    @DataField(pos = 2)
    @NotBlank
    private String name;

    @DataField(pos = 3, name = "image_url")
    @URL
    private String imageUrl;

    @DataField(pos = 4)
    @Digits(integer = 10, fraction = 2)
    @NotNull
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
