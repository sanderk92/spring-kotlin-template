package com.example.config

import com.example.security.user.CurrentUserExtractorAspect
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@EnableAspectJAutoProxy
@Import(value = [CurrentUserExtractorAspect::class])
class EnableAspectOrientedProgramming