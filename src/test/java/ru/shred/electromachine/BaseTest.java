package ru.shred.electromachine;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by KuhtaIA on 22.07.2025
 */
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {
}
