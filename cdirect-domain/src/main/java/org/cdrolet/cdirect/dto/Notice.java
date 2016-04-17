package org.cdrolet.cdirect.dto;

import lombok.Data;
import org.cdrolet.cdirect.type.NoticeType;

/**
 * Created by c on 4/15/16.
 */
@Data
public class Notice {
    private static final long serialVersionUID = 3080925569209286979L;

    private NoticeType type;
}
