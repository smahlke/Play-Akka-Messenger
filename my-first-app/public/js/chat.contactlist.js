function formatPerson(data) {
			console.log(data);
			if (!data.id) {
				return data.text; // Optionsgruppe
			}

			var icon = 'fa fa-user fa-lg';

			if (data.disabled == true) {
				icon = 'fa fa-user-times fa-lg';
			}

			return '<span class="'+icon+'" aria-hidden="true"></span> '
					+ data.text;
		}