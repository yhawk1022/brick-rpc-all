package priv.brick.dto;

import java.math.BigDecimal;

/**
 * @author : pangyan
 * @date : 2022/11/23
 * @description :
 */
public class OrderDTO {
    private Long orderId;

    private Integer status;

    private BigDecimal amount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
