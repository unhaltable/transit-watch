#include "pebble.h"

#define NUM_MENU_SECTIONS 1 // Some arbitrary number....
#define NUM_FIRST_MENU_ITEMS 10

static Window *window;
static Window *menu_window;
static BitmapLayer *image_layer;
static GBitmap *image;
int i; // Dummy variable so I can actually compile the thing...
bool on_splash;

// Begin SimpleMenuLayer
static SimpleMenuLayer *main_menu;
static SimpleMenuSection menu_sections[NUM_MENU_SECTIONS];
static SimpleMenuItem first_menu_items[NUM_FIRST_MENU_ITEMS];



static void menu_select_callback(int index, void *context) {
	// TODO
	// This will open up each individual stop's windows...
	first_menu_items[index].subtitle = "Nope.";
	layer_mark_dirty(simple_menu_layer_get_layer(main_menu));
}

static void up_click_handler(ClickRecognizerRef recognizer, void *context) {
	// TODO
	i = 0;
}

static void down_click_handler(ClickRecognizerRef recognizer, void *context) {
	// TODO
	i = 0;
}

static void select_click_handler(ClickRecognizerRef recognizer, void *context) {
	// TODO
	if(on_splash) {
		menu_window = window_create();
  		window_set_window_handlers(window, (WindowHandlers) {
    		.load = window_load,
    		.unload = window_unload,
  		});
  		window_stack_push(menu_window, true);
	}

}

static void config_provider(void *context) { // gets the proper click handlers for each button
	window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
	window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
	window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
	}

static void window_load(Window *window) {

	int num_a_items = 0;

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "5 N Avenue Rd",
		.subtitle = "Queen's Park @ Museum Station",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "43 N Kennedy Rd",
		.subtitle = "Cardall Avenue",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "190 TranScarberia Express",
		.subtitle = "Sheppard Avenue",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "510 Carlton Rd",
		.subtitle = "UofT @ St. George Street",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "96 A Wilson",
		.subtitle = "York Mills Station",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "69 S Fake Rd",
		.subtitle = "Next stop @ Your Mom's House",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "1337 N Maturity Rd",
		.subtitle = "Jokes on you @ there is none",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "49 W Dunbar Loop",
		.subtitle = "UBC @ Pacific Spirit Park",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "100 E Marine Drive",
		.subtitle = "Canada Line @ Marine Drive Station",
		.callback = menu_select_callback,
	};

	first_menu_items[num_a_items++] = (SimpleMenuItem) {
		.title = "41 W UBC",
		.subtitle = "Joyce Station",
		.callback = menu_select_callback,
	};

	menu_sections[0] = (SimpleMenuSection) {
		.title = "Favorited Stops",
		.num_items = NUM_FIRST_MENU_ITEMS,
		.items = first_menu_items,
	};

	Layer *menu_layer = window_get_root_layer(menu_window);
	GRect menu_bounds = layer_get_frame(menu_layer);
	main_menu = simple_menu_layer_create(menu_bounds, menu_window, menu_sections, NUM_MENU_SECTIONS, NULL);
	layer_add_child(window_layer, simple_menu_layer_get_layer(main_menu));
}

void window_unload(Window *window) {
	simple_menu_layer_destroy(main_menu);

}

static void init() {
	on_splash = true;
	window = window_create();
	window_stack_push(window, true);
	window_set_background_color(window, GColorBlack);

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

static void deinit() {
	gbitmap_destroy(image);
  	bitmap_layer_destroy(image_layer);
  	window_destroy(window);
}

int main(void) {

	init();
	app_event_loop();
	deinit();
}