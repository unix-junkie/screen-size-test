/*-
 * $Id$
 */
package com.example

import java.awt.DisplayMode
import java.awt.DisplayMode.BIT_DEPTH_MULTI
import java.awt.GraphicsConfiguration
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Toolkit

/**
 * @author Andrey ``Bass'' Shcheglov &lt;mailto:andrewbass@gmail.com&gt;
 */
internal object ScreenSizeTest {
	@JvmStatic
	fun main(vararg args: String) {
		println("Legacy screen size:")
		Toolkit.getDefaultToolkit().let { tk ->
			println("\t" + tk.screenSize.let { "${it.width}x${it.height} px" } + " at ${tk.screenResolution} dpi")
		}

		GraphicsEnvironment.getLocalGraphicsEnvironment().let { env ->
			println("Virtual screen size:")
			val deviceBounds: List<Rectangle> = GraphicsEnvironment
					.getLocalGraphicsEnvironment()
					.screenDevices
					.map(GraphicsDevice::getDefaultConfiguration)
					.map(GraphicsConfiguration::getBounds)

			val screenWidth = deviceBounds.map { it.x + it.width }.max()
					?: 0
			val screenHeight = deviceBounds.map { it.y + it.height }.max()
					?: 0
			println("\t${screenWidth}x$screenHeight px")

			println("Default screen device:")
			printDeviceDetails(env.defaultScreenDevice)

			println("All screen devices:")
			env.screenDevices.forEach {
				printDeviceDetails(it)
			}
		}
	}

	private fun printDeviceDetails(device: GraphicsDevice) {
		println("\tScreen ${device.iDstring}")
		println("\t\tConfigurations:")
		println()
		printBounds(device.defaultConfiguration.bounds, "default")
		device.configurations
				.map(GraphicsConfiguration::getBounds)
				.sortedByDescending {
					it.width * it.height + it.width
				}
				.distinctBy {
					it.width * it.height + it.width
				}
				.forEach {
					printBounds(it)
				}
		println()
		println("\t\tDisplay Modes:")
		println()
		printDisplayModeDetails(device.displayMode, "current")
		device.displayModes.forEach {
			printDisplayModeDetails(it)
		}
	}

	private fun printBounds(rect: Rectangle, tag: String? = null) {
		println("\t\t${rect.width}x${rect.height}" + when (tag) {
			null -> ""
			else -> " [$tag]"
		})
	}

	private fun printDisplayModeDetails(mode: DisplayMode, tag: String? = null) {
		val bitDepth: String = when (mode.bitDepth) {
			BIT_DEPTH_MULTI -> DisplayMode::BIT_DEPTH_MULTI.name
			else -> "${mode.bitDepth}"
		}
		println("\t\t${mode.width}x${mode.height} ($bitDepth bpp, ${mode.refreshRate} Hz)" + when (tag) {
			null -> ""
			else -> " [$tag]"
		})
	}
}
