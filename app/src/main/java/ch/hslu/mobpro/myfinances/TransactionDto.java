package ch.hslu.mobpro.myfinances;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TransactionDto {
    public static final String DateFormat = "dd-MM-yyyy";

    private long id;
    private Date date;
    private float amount;
    private String description;
    private CategoryDto category;
    private AccountDto account;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        float tempAmount = amount;
        if (!category.isGain())
        {
            tempAmount = -tempAmount;
        }

        return String.format("%s   Fr: %.2f %nWas: %s | Wie: %s", format.format(date), tempAmount, category.toString(), account.getName());
    }
}
