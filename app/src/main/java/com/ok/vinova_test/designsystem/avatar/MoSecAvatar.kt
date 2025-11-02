package com.ok.vinova_test.designsystem.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.ok.vinova_test.R

/**
 * Design-system avatar component that encapsulates MoSec visual language.
 *
 * The component supports:
 * - Optional accent ring to highlight emphasis or activity state
 * - Optional status indicator (with or without icon)
 * - Optional badge for secondary actions (e.g. edit)
 * - Grayscale rendering for disabled / inactive avatars
 *
 * Consumers provide the primary image via [painter]; all overlays are configured with value objects.
 */
@Composable
fun MoSecAvatar(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = MoSecAvatarDefaults.ContainerSize,
    ring: MoSecAvatarRing? = null,
    badge: MoSecAvatarBadge? = null,
    statusIndicator: MoSecAvatarStatusIndicator? = null,
    grayscale: Boolean = false,
    backgroundColor: Color = MoSecAvatarDefaults.backgroundColor(),
    onClick: (() -> Unit)? = null,
) {
    var avatarModifier = modifier.size(size)
    ring?.let {
        avatarModifier = avatarModifier
            .border(it.width, it.color, CircleShape)
            .padding(it.width)
    }

    avatarModifier = avatarModifier.clip(CircleShape)

    val clickableModifier = onClick?.let {
        val interactionSource = remember { MutableInteractionSource() }
        Modifier
            .semantics { role = Role.Button }
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = true, radius = size / 2)
            ) { it() }
    } ?: Modifier

    val colorFilter = remember(grayscale) {
        if (grayscale) {
            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
        } else {
            null
        }
    }

    Box(
        modifier = avatarModifier
            .background(backgroundColor)
            .then(clickableModifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            colorFilter = colorFilter
        )

        badge?.let { AvatarBadgeOverlay(it) }
        statusIndicator?.let { StatusIndicatorOverlay(it) }
    }
}

@Immutable
data class MoSecAvatarRing(
    val color: Color,
    val width: Dp = 4.dp,
)

@Immutable
data class MoSecAvatarBadge(
    val icon: Painter,
    val contentDescription: String? = null,
    val backgroundColor: Color,
    val iconTint: Color = Color.White,
    val size: Dp = 24.dp,
    val borderColor: Color = Color.White,
    val borderWidth: Dp = 2.dp,
    val alignment: Alignment = Alignment.BottomEnd,
    val offset: DpOffset = DpOffset(0.dp, 0.dp),
    val innerPadding: Dp = 4.dp,
    val elevation: Dp = 2.dp,
)

@Immutable
data class MoSecAvatarStatusIndicator(
    val backgroundColor: Color,
    val icon: Painter? = null,
    val contentDescription: String? = null,
    val iconTint: Color = Color.White,
    val size: Dp = 18.dp,
    val borderColor: Color = Color.White,
    val borderWidth: Dp = 2.dp,
    val alignment: Alignment = Alignment.BottomEnd,
    val offset: DpOffset = DpOffset(0.dp, 0.dp),
    val innerPadding: Dp = 4.dp,
    val elevation: Dp = 2.dp,
)

object MoSecAvatarDefaults {
    val ContainerSize: Dp = 72.dp
    val AccentOrange = Color(0xFFF2994A)
    val AccentBlue = Color(0xFF53B1FD)

    @Composable
    fun backgroundColor(): Color = MaterialTheme.colors.surface

    fun ring(color: Color, width: Dp = 4.dp) = MoSecAvatarRing(color = color, width = width)

    @Composable
    fun editBadge(backgroundColor: Color, tint: Color = Color.White) = MoSecAvatarBadge(
        icon = rememberVectorPainter(Icons.Rounded.Create),
        backgroundColor = backgroundColor,
        iconTint = tint
    )

    fun presenceIndicator(color: Color) = MoSecAvatarStatusIndicator(
        backgroundColor = color,
        icon = null,
        iconTint = Color.Transparent
    )

    @Composable
    fun verifiedIndicator(color: Color) = MoSecAvatarStatusIndicator(
        backgroundColor = color,
        icon = rememberVectorPainter(Icons.Rounded.Check),
        iconTint = Color.White
    )
}

@Composable
private fun BoxScope.AvatarBadgeOverlay(badge: MoSecAvatarBadge) {
    Surface(
        modifier = Modifier
            .align(badge.alignment)
            .offset(badge.offset.x, badge.offset.y)
            .size(badge.size),
        shape = CircleShape,
        color = badge.backgroundColor,
        border = BorderStroke(badge.borderWidth, badge.borderColor),
        elevation = badge.elevation
    ) {
        Icon(
            painter = badge.icon,
            contentDescription = badge.contentDescription,
            tint = badge.iconTint,
            modifier = Modifier
                .matchParentSize()
                .padding(badge.innerPadding),
        )
    }
}

@Composable
private fun BoxScope.StatusIndicatorOverlay(indicator: MoSecAvatarStatusIndicator) {
    Surface(
        modifier = Modifier
            .align(indicator.alignment)
            .offset(indicator.offset.x, indicator.offset.y)
            .size(indicator.size),
        shape = CircleShape,
        color = indicator.backgroundColor,
        border = BorderStroke(indicator.borderWidth, indicator.borderColor),
        elevation = indicator.elevation
    ) {
        indicator.icon?.let { icon ->
            Icon(
                painter = icon,
                contentDescription = indicator.contentDescription,
                tint = indicator.iconTint,
                modifier = Modifier
                    .matchParentSize()
                    .padding(indicator.innerPadding),
            )
        }
    }
}

@Preview(name = "MoSec Avatar - Actions", showBackground = true, backgroundColor = 0xFF160F35)
@Composable
private fun MoSecAvatarActionsPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .background(Color(0xFF160F35))
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoSecAvatar(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Editable avatar",
                ring = MoSecAvatarDefaults.ring(MoSecAvatarDefaults.AccentOrange),
                badge = MoSecAvatarDefaults
                    .editBadge(MoSecAvatarDefaults.AccentOrange)
                    .copy(offset = DpOffset(6.dp, 6.dp))
            )

            MoSecAvatar(
                painter = rememberVectorPainter(Icons.Rounded.Person),
                contentDescription = "Editable avatar alt",
                ring = MoSecAvatarDefaults.ring(MoSecAvatarDefaults.AccentOrange),
                badge = MoSecAvatarDefaults
                    .editBadge(MoSecAvatarDefaults.AccentOrange)
                    .copy(offset = DpOffset(6.dp, 6.dp)),
                backgroundColor = Color(0xFF2C2453)
            )

            MoSecAvatar(
                painter = rememberVectorPainter(Icons.Rounded.Person),
                contentDescription = "Disabled avatar",
                ring = MoSecAvatarDefaults.ring(Color(0xFF9E9E9E)),
                badge = MoSecAvatarBadge(
                    icon = rememberVectorPainter(Icons.Rounded.Create),
                    backgroundColor = Color(0xFF9E9E9E),
                    offset = DpOffset(6.dp, 6.dp)
                ),
                grayscale = true,
                backgroundColor = Color(0xFF2C2453)
            )
        }
    }
}

@Preview(name = "MoSec Avatar - Presence", showBackground = true, backgroundColor = 0xFF160F35)
@Composable
private fun MoSecAvatarPresencePreview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .background(Color(0xFF160F35))
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MoSecAvatar(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Online status",
                ring = MoSecAvatarDefaults.ring(MoSecAvatarDefaults.AccentOrange),
                statusIndicator = MoSecAvatarDefaults
                    .presenceIndicator(Color(0xFFF4B740))
                    .copy(offset = DpOffset(6.dp, 6.dp))
            )

            MoSecAvatar(
                painter = rememberVectorPainter(Icons.Rounded.Person),
                contentDescription = "Focus status",
                ring = MoSecAvatarDefaults.ring(MoSecAvatarDefaults.AccentOrange),
                statusIndicator = MoSecAvatarDefaults
                    .presenceIndicator(Color(0xFFEF6F3C))
                    .copy(offset = DpOffset(6.dp, 6.dp)),
                backgroundColor = Color(0xFF2C2453)
            )

            MoSecAvatar(
                painter = rememberVectorPainter(Icons.Rounded.Person),
                contentDescription = "Verified",
                ring = MoSecAvatarDefaults.ring(MoSecAvatarDefaults.AccentBlue),
                statusIndicator = MoSecAvatarDefaults
                    .verifiedIndicator(MoSecAvatarDefaults.AccentBlue)
                    .copy(offset = DpOffset(6.dp, 6.dp)),
                backgroundColor = Color(0xFF2C2453)
            )
        }
    }
}
