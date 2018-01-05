/**
 * Coder: richie
 * Date: 14-4-24
 * Time: 11:13
 */
name_widgets[_name]=_widget;
_widget.options = {};
_widget.options.form = {};
_widget.options.form.getWidgetByName = function(name) {
    return name_widgets[name];
};