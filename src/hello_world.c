#include <pebble.h>

Window *window;
TextLayer *text_layer;

// stops_data[i][j] is the j-th field of the i-th stop
char*** stops_data = NULL;
unsigned int num_stops = 0, num_fields_per_stop = 0;

// Possible message types
#define MESSAGE_TYPE 0
enum {
    MESSAGE_STOPS_DATA
};

// Data fields for stops data
enum {
    NUM_STOPS = 1,
    NUM_FIELDS_PER_STOP,
    IDX_BEGIN_STOPS_DATA
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
    num_stops = 0;
    num_fields_per_stop = 0;
}

void initialize_stops_data(DictionaryIterator *received, void *context)
{
    destroy_stops_data();

    // Get the number of stops we're receiving
    Tuple *tuple = dict_find(received, NUM_STOPS);
    if (tuple)
        num_stops = tuple->value->uint32;
    else return;

    // Get the number of data fields per stop
    tuple = dict_find(received, NUM_FIELDS_PER_STOP);
    if (tuple)
        num_fields_per_stop = tuple->value->uint32;
    else return;

    // Allocate stops_data according to these sizes
    stops_data = (char***)malloc(num_stops * sizeof(char**));
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Expecting %d number of stops with %d fields per stop...",
            num_stops, num_fields_per_stop);
    
    for (unsigned int i = 0; i < num_stops; i++)
    {
        stops_data[i] = (char**)malloc(num_fields_per_stop * sizeof(char*));
        for (unsigned int j = 0; j < num_fields_per_stop; j++)
        {
            tuple = dict_find(received, i*j + IDX_BEGIN_STOPS_DATA);
            if (tuple)
            {
                char* data_str = tuple->value->cstring;
                stops_data[i][j] = (char*)malloc((strlen(data_str)+1) * sizeof(char));
                strcpy(stops_data[i][j], data_str);
            }
            else
                APP_LOG(APP_LOG_LEVEL_DEBUG, "Missing data (stop %d, field %d)!", i, j);
        }
    }
}

// outgoing data was delivered
void out_sent_handler(DictionaryIterator *sent, void *context){}
// outgoing data failed
void out_failed_handler(DictionaryIterator *failed, AppMessageResult reason, void *context){}
// incoming data dropped
void in_dropped_handler(AppMessageResult reason, void *context){}

// incoming data received
void in_received_handler(DictionaryIterator *received, void *context)
{
    Tuple *tuple = dict_find(received, MESSAGE_TYPE);
    if (!tuple)
    {
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Missing message type!");
        return;
    }

    unsigned int message_type = tuple->value->uint32;
    switch(message_type)
    {
    case MESSAGE_STOPS_DATA:
        initialize_stops_data(received, context);
        break;
    default:
        APP_LOG(APP_LOG_LEVEL_DEBUG, "Unknown message type %d", message_type);
    }
}


void handle_init(void)
{
    // Register AppMessage handlers + initialize
    app_message_register_inbox_received(in_received_handler);
    app_message_register_inbox_dropped(in_dropped_handler);
    app_message_register_outbox_sent(out_sent_handler);    
    app_message_register_outbox_failed(out_failed_handler);
    app_message_open(app_message_inbox_size_maximum(), app_message_outbox_size_maximum());
    
    // Create a window and text layer
    window = window_create();
    text_layer = text_layer_create(GRect(0, 0, 144, 154));
	
    // Set the text, font, and text alignment
    text_layer_set_text(text_layer, "Hi, I'm a Pebble!");
    text_layer_set_font(text_layer, fonts_get_system_font(FONT_KEY_GOTHIC_28_BOLD));
    text_layer_set_text_alignment(text_layer, GTextAlignmentCenter);
	
    // Add the text layer to the window
    layer_add_child(window_get_root_layer(window), text_layer_get_layer(text_layer));

    // Push the window
    window_stack_push(window, true);
	
    // App Logging!
    APP_LOG(APP_LOG_LEVEL_DEBUG, "Just pushed a window!");
}

void handle_deinit(void)
{
    app_message_deregister_callbacks();
    text_layer_destroy(text_layer);
    window_destroy(window);

    destroy_stops_data();
}

int main(void) {
    handle_init();
    app_event_loop();
    handle_deinit();
}
