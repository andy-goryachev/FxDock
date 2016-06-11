# CPane

CPane is an FX Pane implementation that combines BorderPane and a table layout similar to GridPane.  
The grid (or, rather, table) layout is similar to my Swing
[CPanel](https://github.com/andy-goryachev/PasswordSafe/blob/master/src/goryachev/common/ui/CPanel.java), 
which in turn was inspired by 
[info.clearthought.TableLayout](http://www.clearthought.info/)

![screenshot](https://github.com/andy-goryachev/FxDock/blob/master/screenshots/2016-0611-153944-977.png)

## Example

		CPane p = new CPane();
		p.setGaps(10, 5);
		p.setPadding(10);
		p.addColumns
		(
			10,
			CPane.PREF,
			CPane.FILL,
			CPane.PREF,
			10
		);
		p.addRows
		(
			10,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.PREF,
			CPane.FILL,
			10
		);
		int r = 1;
		p.add(1, r, 3, 1, FX.label(info, FxCtl.WRAP_TEXT));
		r++;
		p.add(1, r, FX.label("User name:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, userNameField);
		r++;
		p.add(1, r, FX.label("Password:", TextAlignment.RIGHT));
		p.add(2, r, 2, 1, passwordField);
		r++;
		p.add(3, r, loginButton);

## License

This project and its source code is licensed under the [GNU General Public License version 3](https://www.gnu.org/licenses/gpl-3.0.en.html) and you should feel free to make adaptations of this work. Please see the included LICENSE file for further details.

Please contact me to acquire this library under a business-friendly license.


