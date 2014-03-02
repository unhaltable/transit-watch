#include "pebble.h"
#include "pebble_os.h"
#include "pebble_app.h"
#include "pebble_fonts.h"

static Window *window; 
static BitmapLayer *image_layer;
static GBitmap *image;


static void up_click_handler {
	// TODO
}

static void down_click_handler {
	// TODO
}

static void select_click_handler {
	// TODO
}

static void config_provider(void *context) { // gets the proper click handlers for each button
	window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
	window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
	window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
	}

static void init {
	window = window_create();
	window_stack_push(window, true);
	window_set_color_background_color(window, GColorBlack);

	window_set_click_config_provider(window, config_provider);

	Layer *window_layer = window_get_root_layer(window); // Set window_layer to base canvas
	GRect bounds = layer_get_frame(window_layer); // creates a GRect struct that is the size of the frame of window_layer

	// Don't forget to deinit this
	image = gbitmap_create_with_resource(RESOURCE_ID_IMAGE_LOGO);
	
	image_layer = bitmap_layer_create(bounds);
	bitmap_layer_set_bitmap(image_layer, image);
	bitmap_layer_set_alignment(image_layer, GAlignCenter);
	layer_add_child(window_layer, bitmap_layer_get_layer(image_layer));

}

static void deinit {
	gbitmap_destroy(image);
  	bitmap_layer_destroy(image_layer);
  	window_destroy(window);
}

int main(void) {
	init();
	app_event_loop();
	deinit();
}