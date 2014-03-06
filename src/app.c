#include <pebble.h>

#define NUM_MENU_SECTIONS 1 // Some arbitrary number....

static Window *window;
static Window *menu_window;
static Window *stop_window;

static BitmapLayer *image_layer;
static GBitmap *image;

// Begin SimpleMenuLayer
static SimpleMenuLayer *main_menu;
static SimpleMenuSection menu_sections[NUM_MENU_SECTIONS];
static SimpleMenuItem* first_menu_items;

static TextLayer *stop_title;
static TextLayer *stop_subtitle;
static TextLayer *stop_weather;
static TextLayer *stop_ETA;

static Layer *raw_layer;

bool on_splash;

// stops_data[i][j] is the j-th field of the i-th stop
char*** stops_data = NULL;
unsigned int i = 0, num_stops = 0, num_fields_per_stop = 0;

// Possible message types
#define MESSAGE_TYPE 0
enum {
    MESSAGE_STOPS_METADATA,
    MESSAGE_STOP_DATA
};

// Data fields for stops data
enum {
    NUM_STOPS = 1,
    NUM_FIELDS_PER_STOP = 2
};

unsigned int IDX_BEGIN_STOPS_DATA = 1;

// Data fields per stop
enum {
    ROUTE_TAG,
    ROUTE_TITLE,
    DIRECTION_NAME,
    STOP_TITLE
};

void destroy_stops_data()
{
    // Deallocate existing stops_data
    for (unsigned int i = 0; i < num_stops; i++)
    {
        for (unsigned int j = 0; j < num_fields_per_stop; j++)
            if (stops_data[i][j])
                free(stops_data[i][j]);
        free(stops_data[i]);
    }
    if (stops_data)
        free(stops_data);
    if (first_menu_items)
        free(first_menu_items);
    num_stops = 0;
    num_fields_per_stop = 0;
}

void initialize_stops_data(DictionaryIterator *received, void *context)
{
    // destroy_stops_data();

    // // Get the number of stops we're receiving
    // Tuple *tuple = dict_find(received, NUM_STOPS);
    // if (tuple)
    //     num_stops = tuple->value->uint32;
    // else
    //     num_stops = 0;

    // // Get the number of data fields per stop
    // tuple = dict_find(received, NUM_FIELDS_PER_STOP);
    // if (tuple)
    //     num_fields_per_stop = tuple->value->uint32;
    // else
    //     num_fields_per_stop = 0;

    // if (num_stops == 0 || num_fields_per_stop == 0)
    // {
    //     APP_LOG(APP_LOG_LEVEL_DEBUG, "Malformed: NUM_STOPS: %d, NUM_FIELDS_PER_STOP: %d",
    //             num_stops, num_fields_per_stop);
    //     return;
    // }

    // // Allocate stops_data according to these sizes
    // stops_data = (char***) malloc(num_stops * sizeof(char**));
    // APP_LOG(APP_LOG_LEVEL_DEBUG, "Expecting %d number of stops with %d fields per stop...",
    //         num_stops, num_fields_per_stop);

    // for (unsigned int i = 0; i < num_stops; i++)
    // {
    //     stops_data[i] = (char**)malloc(num_fields_per_stop * sizeof(char*));
    //     for (unsigned int j = 0; j < num_fields_per_stop; j++)
    //     {
    //         tuple = dict_find(received, i*num_fields_per_stop + j + IDX_BEGIN_STOPS_DATA);
    //         if (tuple)
    //         {
    //             char* data_str = tuple->value->cstring;
    //             stops_data[i][j] = (char*)malloc((strlen(data_str)+1) * sizeof(char));
    //             strcpy(stops_data[i][j], data_str);

    //             APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
    //                     i, j, stops_data[i][j]);
    //         }
    //         else
    //             APP_LOG(APP_LOG_LEVEL_DEBUG, "Missing data (stop %d, field %d)!", i, j);
    //     }
    // }
}

char *translate_error(AppMessageResult result) {
    switch (result) {
    case APP_MSG_OK: return "APP_MSG_OK";
    case APP_MSG_SEND_TIMEOUT: return "APP_MSG_SEND_TIMEOUT";
    case APP_MSG_SEND_REJECTED: return "APP_MSG_SEND_REJECTED";
    case APP_MSG_NOT_CONNECTED: return "APP_MSG_NOT_CONNECTED";
    case APP_MSG_APP_NOT_RUNNING: return "APP_MSG_APP_NOT_RUNNING";
    case APP_MSG_INVALID_ARGS: return "APP_MSG_INVALID_ARGS";
    case APP_MSG_BUSY: return "APP_MSG_BUSY";
    case APP_MSG_BUFFER_OVERFLOW: return "APP_MSG_BUFFER_OVERFLOW";
    case APP_MSG_ALREADY_RELEASED: return "APP_MSG_ALREADY_RELEASED";
    case APP_MSG_CALLBACK_ALREADY_REGISTERED: return "APP_MSG_CALLBACK_ALREADY_REGISTERED";
    case APP_MSG_CALLBACK_NOT_REGISTERED: return "APP_MSG_CALLBACK_NOT_REGISTERED";
    case APP_MSG_OUT_OF_MEMORY: return "APP_MSG_OUT_OF_MEMORY";
    case APP_MSG_CLOSED: return "APP_MSG_CLOSED";
    case APP_MSG_INTERNAL_ERROR: return "APP_MSG_INTERNAL_ERROR";
    default: return "UNKNOWN ERROR";
    }
}

// outgoing data was delivered
void out_sent_handler(DictionaryIterator *sent, void *context){}
// outgoing data failed
void out_failed_handler(DictionaryIterator *failed, AppMessageResult reason, void *context){}
// incoming data dropped
void in_dropped_handler(AppMessageResult reason, void *context)
{
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Incoming message dropped (%s)", translate_error(reason));
}

// incoming data received
void in_received_handler(DictionaryIterator *received, void *context)
{
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Received a message");
    Tuple *tuple = dict_find(received, MESSAGE_TYPE);
    if (!tuple)
    {
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Missing message type!");
        return;
    }

    unsigned int message_type = tuple->value->uint32;
    switch(message_type)
    {
    case MESSAGE_STOPS_METADATA:
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Received a MESSAGE_STOPS_METADATA. Syncing...");

        // Delete existing data
        destroy_stops_data();

        i = 0;

        // Get the number of stops we're receiving
        Tuple *tuple = dict_find(received, NUM_STOPS);
        num_stops = tuple ? tuple->value->uint32 : 0;

        // Get the number of data fields per stop
        tuple = dict_find(received, NUM_FIELDS_PER_STOP);
        num_fields_per_stop = tuple ? tuple->value->uint32 : 0;

        APP_LOG(APP_LOG_LEVEL_DEBUG, "Expecting %d number of stops with %d fields per stop...",
        num_stops, num_fields_per_stop);

        stops_data = (char***) malloc(num_stops * sizeof(char**));
        break;
    case MESSAGE_STOP_DATA:
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Received a MESSAGE_STOP_DATA...");
        if (num_stops > 0) {
            if (i < num_stops)
            {
                // Parse received stop data
                stops_data[i] = (char**) malloc(num_fields_per_stop * sizeof(char*));
                for (unsigned int j = 0; j < num_fields_per_stop; j++)
                {
                    tuple = dict_find(received, j + IDX_BEGIN_STOPS_DATA);
                    if (tuple)
                    {
                        char* data_str = tuple->value->cstring;
                        stops_data[i][j] = (char*) malloc((strlen(data_str) + 1) * sizeof(char));
                        strcpy(stops_data[i][j], data_str);

                        APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
                                i, j, stops_data[i][j]);
                    }
                    else
                        APP_LOG(APP_LOG_LEVEL_DEBUG, "Missing data (stop %d, field %d)!", i, j);
                }
                i++;
            } else if (i == num_stops) {
                // Done loading stops
                first_menu_items = malloc(num_stops * sizeof(SimpleMenuItem));
                if (on_splash)
                {
                    on_splash = false;
                    window_stack_push(menu_window, true);
                    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just pushed a window!");
                    window_stack_remove(window, true);
                    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just removed a window!");
                }
            }
        } else {
            APP_LOG(APP_LOG_LEVEL_DEBUG, "Too many stops received.");
        }
        break;
    default:
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Unknown message type %d", message_type);
    }
}

static void menu_select_callback(int index, void *context) {
    // TODO
    // This will open up each individual stop's windows...
    //first_menu_items[index].subtitle = "Nope.";
    //layer_mark_dirty(simple_menu_layer_get_layer(main_menu));

    // Set stop_window's fields to selected stop's data
    if (stops_data && index < (int)num_stops && num_fields_per_stop > 3)
    {
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Loading data for stop %d into stop_window", index);
        text_layer_set_text(route_title, stops_data[index][1]);
        text_layer_set_text(stop_title, stops_data[index][2]);
        text_layer_set_text(stop_weather, stops_data[index][3]);
        text_layer_set_text(stop_ETA, stops_data[index][4]);
    }

    // Show the window
    window_stack_push(stop_window, true);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just pushed a window!");
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
    i = 0; // ??
}

static void config_provider(void *context) { // gets the proper click handlers for each button
    window_single_click_subscribe(BUTTON_ID_UP, up_click_handler);
    window_single_click_subscribe(BUTTON_ID_DOWN, down_click_handler);
    window_single_click_subscribe(BUTTON_ID_SELECT, select_click_handler);
}

static void window_load(Window *window)
{
    unsigned int i;
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Loading %d items into menu...", num_stops);
    for (i = 0; i < num_stops; i++)
    {
        first_menu_items[i] = (SimpleMenuItem) {
            .title = stops_data[i][ROUTE_TITLE],
            .subtitle = stops_data[i][STOP_TITLE],
            .callback = menu_select_callback,
        };
    }

    menu_sections[0] = (SimpleMenuSection) {
        .title = "Favourited Stops",
        .num_items = num_stops,
        .items = first_menu_items,
    };

    Layer *menu_layer = window_get_root_layer(window);
    GRect menu_bounds = layer_get_frame(menu_layer);
    main_menu = simple_menu_layer_create(menu_bounds, window, menu_sections, NUM_MENU_SECTIONS, NULL);
    layer_add_child(menu_layer, simple_menu_layer_get_layer(main_menu));
}

void window_unload(Window *window) {
    simple_menu_layer_destroy(main_menu);
}

// static void layer_update_callback(Layer *me, GContext *ctx) {
//     GRect myRect = image->bounds;
//     graphics_context_set_fill_color(ctx, GColorWhite);
//     graphics_draw_round_rect(ctx, myRect, 16);
// }

void handle_init(void)
{
    on_splash = true;

    // Register AppMessage handlers + initialize
    // app_message_register_inbox_received(in_received_handler);
    // app_message_register_inbox_dropped(in_dropped_handler);
    // app_message_register_outbox_sent(out_sent_handler);
    // app_message_register_outbox_failed(out_failed_handler);
    // app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());

    // Send initial message to notify the app has started
    // DictionaryIterator *iter;
    // app_message_outbox_begin(&iter);
    // Tuplet value = TupletInteger(MESSAGE_TYPE, 1);
    // dict_write_tuplet(iter, &value);
    // app_message_outbox_send();

    // Creation of Main Menu
    menu_window = window_create();
    window_set_window_handlers(menu_window, (WindowHandlers) {
        .load = window_load,
        .unload = window_unload,
    });

    // Creation of stop_window
    stop_window = window_create();
    window_set_background_color(stop_window, GColorBlack);
    Layer *stop_root = window_get_root_layer(stop_window);
    GRect stop_bounds = layer_get_frame(stop_root);

    // Set route_title
    route_title = text_layer_create(GRect(0, 0, stop_bounds.size.w, 28));
    text_layer_set_text(route_title, "5 N Avenue Rd");
    //text_layer_set_text(route_title, pointer to string) this is for when we implement non-dummy values
    text_layer_set_font(route_title, fonts_get_system_font(FONT_KEY_GOTHIC_28_BOLD));
    text_layer_set_text_alignment(route_title, GTextAlignmentCenter);
    text_layer_set_overflow_mode(route_title, GTextOverflowModeTrailingEllipsis);
    text_layer_set_text_color(route_title, GColorWhite);
    text_layer_set_background_color(route_title, GColorClear);
    layer_add_child(stop_root, text_layer_get_layer(route_title));

    // Set stop_subtitle
    stop_subtitle = text_layer_create(GRect(0, 28, stop_bounds.size.w, 48));
    text_layer_set_text(stop_subtitle, "Queen's Park @ Museum Station");
    //text_layer_set_text(stop_title, foo) see above comment
    text_layer_set_font(stop_subtitle, fonts_get_system_font(FONT_KEY_GOTHIC_24));
    text_layer_set_text_alignment(stop_subtitle, GTextAlignmentCenter);
    text_layer_set_overflow_mode(stop_subtitle, GTextOverflowModeWordWrap);
    text_layer_set_text_color(stop_subtitle, GColorWhite);
    text_layer_set_background_color(stop_subtitle, GColorClear);
    layer_add_child(stop_root, text_layer_get_layer(stop_subtitle));

    // Set stop_weather
    stop_weather = text_layer_create(GRect(0, 82, stop_bounds.size.w, 18));
    text_layer_set_text(stop_weather, "5°C, Scattered Flurries");
    //text_layer_set_text(stop_weather, foo) see above comment
    text_layer_set_font(stop_weather, fonts_get_system_font(FONT_KEY_GOTHIC_18_BOLD));
    text_layer_set_text_alignment(stop_weather, GTextAlignmentCenter);
    text_layer_set_overflow_mode(stop_weather, GTextOverflowModeTrailingEllipsis);
    text_layer_set_text_color(stop_weather, GColorWhite);
    text_layer_set_background_color(stop_weather, GColorClear);
    layer_add_child(stop_root, text_layer_get_layer(stop_weather));

    // Set stop_ETA
    // raw_layer = layer_create(stop_bounds);
    // layer_set_update_proc(raw_layer, layer_update_callback);
    // layer_add_child(stop_root, raw_layer);

    stop_ETA = text_layer_create(GRect(0, 110, stop_bounds.size.w, stop_bounds.size.h));
    text_layer_set_text(stop_ETA, "15 min");
    //text_layer_set_text(stop_ETA, foo) see above comment
    text_layer_set_font(stop_ETA, fonts_get_system_font(FONT_KEY_BITHAM_30_BLACK));
    text_layer_set_text_alignment(stop_ETA, GTextAlignmentCenter);
    text_layer_set_overflow_mode(stop_ETA, GTextOverflowModeTrailingEllipsis);
    // text_color is by default black
    text_layer_set_background_color(stop_ETA, GColorWhite);
    layer_add_child(stop_root, text_layer_get_layer(stop_ETA));

    // Creation of Splash Screen
    window = window_create();
    window_stack_push(window, true);
    window_set_background_color(window, GColorBlack);

    APP_LOG(APP_LOG_LEVEL_DEBUG, "Pushed black window");

    window_set_click_config_provider(window, config_provider);

    Layer *window_layer = window_get_root_layer(window); // Set window_layer to base canvas
    GRect bounds = layer_get_frame(window_layer); // creates a GRect struct that is the size of the frame of window_layer

    // Don't forget to deinit this
    image = gbitmap_create_with_resource(RESOURCE_ID_IMAGE_LOGO);

    // Creation of image layer
    image_layer = bitmap_layer_create(bounds);
    bitmap_layer_set_bitmap(image_layer, image);
    bitmap_layer_set_alignment(image_layer, GAlignCenter);
    layer_add_child(window_layer, bitmap_layer_get_layer(image_layer));

    // temp

    // create sample data
    num_stops = 1;
    num_fields_per_stop = 4;
    stops_data = (char***) malloc(num_stops * sizeof(char**));
    // Parse received stop data
    stops_data[0] = (char**) malloc(num_fields_per_stop * sizeof(char*));

    char* data_str;

    data_str = "506 E Carlton";
    stops_data[0][0] = (char*) malloc((strlen(data_str) + 1) * sizeof(char));
    strcpy(stops_data[0][0], data_str);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
            0, 0, stops_data[0][0]);

    data_str = "College St At Beverley St";
    stops_data[0][1] = (char*) malloc((strlen(data_str) + 1) * sizeof(char));
    strcpy(stops_data[0][1], data_str);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
            0, 1, stops_data[0][1]);

    data_str = "-4°C, a few flurries.";
    stops_data[0][2] = (char*) malloc((strlen(data_str) + 1) * sizeof(char));
    strcpy(stops_data[0][2], data_str);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
            0, 2, stops_data[0][2]);

    data_str = "15 min";
    stops_data[0][3] = (char*) malloc((strlen(data_str) + 1) * sizeof(char));
    strcpy(stops_data[0][3], data_str);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Stops data (stop %d, field %d): %s",
            0, 3, stops_data[0][3]);

    first_menu_items = malloc(num_stops * sizeof(SimpleMenuItem));

    on_splash = false;
    window_stack_push(menu_window, true);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just pushed a window!");
    window_stack_remove(window, true);
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just removed a window!");
}

void handle_deinit(void)
{
    app_message_deregister_callbacks();

    gbitmap_destroy(image);
    bitmap_layer_destroy(image_layer);
    window_destroy(window);

    text_layer_destroy(stop_title);
    text_layer_destroy(stop_subtitle);
    text_layer_destroy(stop_weather);
    text_layer_destroy(stop_ETA);

    layer_destroy(raw_layer);
    window_destroy(stop_window);

    destroy_stops_data();
}

int main(void) {
    handle_init();
    app_event_loop();
    handle_deinit();
}
