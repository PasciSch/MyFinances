package ch.hslu.mobpro.myfinances;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TransactionDto {
    public static final String DateFormat = "dd-MM-yyyy";

    private float id;
    private Date date;
    private float amount;
    private String description;
    private CategoryDto category;
    private AccountDto account;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat(DateFormat);
        return String.format("%s %.2f Fr %s %s", format.format(date), amount, account.getName(), category.toString());
    }
}
