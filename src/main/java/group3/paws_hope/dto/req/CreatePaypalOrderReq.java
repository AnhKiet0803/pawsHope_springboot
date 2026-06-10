package group3.paws_hope.dto.req;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePaypalOrderReq {
    private BigDecimal amountUsd;
}