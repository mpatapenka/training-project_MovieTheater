package org.maksim.training.mtapp.entity;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class Message {
    String text;
}