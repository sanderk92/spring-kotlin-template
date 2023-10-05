package com.template.util

import com.template.security.user.CurrentUserAspect
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@EnableAspectJAutoProxy
@Import(value = [CurrentUserAspect::class])
class EnableAspectOrientedProgramming