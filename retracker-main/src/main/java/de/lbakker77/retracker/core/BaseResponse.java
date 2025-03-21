package de.lbakker77.retracker.core;

import lombok.*;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BaseResponse {

    public static BaseResponse ofSuccess() {
        return BaseResponse.builder().success(true).build();
    }

    public static BaseResponse ofFailure(List<Violation> violations) {
        return BaseResponse.builder().success(false).violations(violations).build();
    }

    @Builder.Default
    private boolean success = true;
    @Builder.Default
    private List<Violation> violations = new LinkedList<>();

}


