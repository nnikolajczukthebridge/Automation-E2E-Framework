package models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class TransferNcbCashin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long transfer_id;
    String source_account_id;
    String source_user_id;
    String amount;
    String currency;
    String comments;
    String datetime;
    String status;
    String transfer_type;
    String category;
    String external_reference_id;

    public TransferNcbCashin() {
    }

    public TransferNcbCashin(long transfer_id, String source_account_id, String source_user_id, String amount, String currency, String comments, String datetime, String status, String transfer_type, String category, String external_reference_id) {
        this.transfer_id = transfer_id;
        this.source_account_id = source_account_id;
        this.source_user_id = source_user_id;
        this.amount = amount;
        this.currency = currency;
        this.comments = comments;
        this.datetime = datetime;
        this.status = status;
        this.transfer_type = transfer_type;
        this.category = category;
        this.external_reference_id = external_reference_id;
    }

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getSource_account_id() {
        return source_account_id;
    }

    public void setSource_account_id(String source_account_id) {
        this.source_account_id = source_account_id;
    }

    public String getSource_user_id() {
        return source_user_id;
    }

    public void setSource_user_id(String source_user_id) {
        this.source_user_id = source_user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransfer_type() {
        return transfer_type;
    }

    public void setTransfer_type(String transfer_type) {
        this.transfer_type = transfer_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExternal_reference_id() {
        return external_reference_id;
    }

    public void setExternal_reference_id(String external_reference_id) {
        this.external_reference_id = external_reference_id;
    }
}
