function PeriodType()
{    
    var monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
		'July', 'August', 'September', 'October', 'November', 'December'];
    
	var format_yyyymmdd = function(date) {
		var y = date.getFullYear(),
			m = new String(date.getMonth() + 1),
			d = new String(date.getDate());
		m = m.length < 2 ? '0' + m : m;
		d = d.length < 2 ? '0' + d : d;
		return y + '-' + m + '-' + d;
	};

    this.reverse = function( array )
    {
        var reversed = [];
        var i = 0;

        for ( var j = array.length - 1; j >= 0; j-- )
        {
            reversed[i++] = array[j];
        }

        return reversed;
    };

    this.filterFuturePeriods = function( periods )
    {
        var array = [],
			now = new Date();

        for ( var i = 0; i < periods.length; i++ )
        {
			if ( new Date( periods[i]['startDate'] ) <= now )
            {
                array[i] = periods[i];
            }
        }

        return array;
    };

    var periodTypes = [];
    periodTypes['Daily'] = new DailyPeriodType( format_yyyymmdd );
    periodTypes['Weekly'] = new WeeklyPeriodType( format_yyyymmdd );
    periodTypes['Monthly'] = new MonthlyPeriodType( format_yyyymmdd, monthNames, this.reverse );
    periodTypes['BiMonthly'] = new BiMonthlyPeriodType( format_yyyymmdd, monthNames, this.reverse );
    periodTypes['Quarterly'] = new QuarterlyPeriodType( format_yyyymmdd, monthNames, this.reverse );
    periodTypes['SixMonthly'] = new SixMonthlyPeriodType( monthNames );
    periodTypes['Yearly'] = new YearlyPeriodType( format_yyyymmdd, this.reverse );
    periodTypes['FinancialOct'] = new FinancialOctoberPeriodType( format_yyyymmdd, monthNames, this.reverse );
    periodTypes['FinancialJuly'] = new FinancialJulyPeriodType( format_yyyymmdd, monthNames, this.reverse );
    periodTypes['FinancialApril'] = new FinancialAprilPeriodType( format_yyyymmdd, monthNames, this.reverse );

    this.get = function( key )
    {
        return periodTypes[key];
    };
}

function DailyPeriodType( format_yyyymmdd )
{	
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '01 Jan ' + year );

        while ( date.getFullYear() === year )
        {
            var period = {};
            period['startDate'] = format_yyyymmdd( date );
            period['endDate'] = period['startDate'];
            period['name'] = period['startDate'];
            //period['id'] = 'Daily_' + period['startDate'];
            period['iso'] = period['startDate'].replace( /-/g, '' );
            period['id'] = period['iso'];
            periods.push( period );
            date.setDate( date.getDate() + 1 );
        }

        return periods;
    };
}

function WeeklyPeriodType( format_yyyymmdd )
{	
    this.generatePeriods = function( offset )
    {
		var periods = [];
		var year = new Date().getFullYear() + offset;
		var date = new Date( '01 Jan ' + year );
		var day = date.getDay();
		var week = 1;
		
		if ( day <= 4 )
		{
			date.setDate( date.getDate() - ( day - 1 ) );
		}
		else
		{
			date.setDate( date.getDate() + ( 8 - day ) );
		}
		
		while ( date.getFullYear() <= year )
		{
			var period = {};
			period['startDate'] = format_yyyymmdd( date );
			//period['id'] = 'Weekly_' + period['startDate'];
			period['iso'] = year + 'W' + week;
            period['id'] = period['iso'];
			date.setDate( date.getDate() + 6 );
			period['endDate'] = format_yyyymmdd( date );
			period['name'] = 'W' + week + ' - ' + period['startDate'] + ' - ' + period['endDate'];
			periods.push( period );			
			date.setDate( date.getDate() + 1 );
			week++;
		}
		
        return periods;
    };
}

function MonthlyPeriodType( format_yyyymmdd, monthNames, rev )
{
	var format_iso = function(date) {
		var y = date.getFullYear(),
			m = new String(date.getMonth() + 1);
		m = m.length < 2 ? '0' + m : m;
		return y + m;
	};
	
    this.generatePeriods = function( offset )
    {
		var periods = [];
		var year = new Date().getFullYear() + offset;
		var date = new Date( '31 Dec ' + year );
		
		while ( date.getFullYear() === year )
		{
			var period = {};
			period['endDate'] = format_yyyymmdd( date );
			date.setDate( 1 );
			period['startDate'] = format_yyyymmdd( date );
			period['name'] = monthNames[date.getMonth()] + ' ' + date.getFullYear();
			//period['id'] = 'Monthly_' + period['startDate'];
			period['iso'] = format_iso( date );
            period['id'] = period['iso'];
			periods.push( period );
			date.setDate( 0 );
		}
		
        return rev(periods);
    };
}

function BiMonthlyPeriodType( format_yyyymmdd, monthNames, rev )
{
	var format_iso = function( date ) {
		var y = date.getFullYear(),
			m = new String(date.getMonth() + 1);
		m = m.length < 2 ? '0' + m : m;
		return y + m + 'B';
	};
	
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '31 Dec ' + year );

        while ( date.getFullYear() === year )
        {
            var period = {};
            period['endDate'] = format_yyyymmdd( date );
            date.setDate( 0 );
            date.setDate( 1 );
			period['startDate'] = format_yyyymmdd( date );
            period['name'] = monthNames[date.getMonth()] + ' - ' + monthNames[date.getMonth() + 1] + ' ' + date.getFullYear();
            //period['id'] = 'BiMonthly_' + period['startDate'];
            period['iso'] = format_iso( date );
            period['id'] = period['iso'];
            periods.push(period);
            date.setDate( 0 );
        }

        return rev(periods);
    };
}

function QuarterlyPeriodType( format_yyyymmdd, monthNames, rev )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '31 Dec ' + year );
        var quarter = 4;

        while ( date.getFullYear() === year )
        {
            var period = {};
            period['endDate'] = format_yyyymmdd( date );
            date.setDate( 0 );
            date.setDate( 0 );
            date.setDate( 1 );
			period['startDate'] = format_yyyymmdd( date );
            period['name'] = monthNames[date.getMonth()] + ' - ' + monthNames[date.getMonth() + 2] + ' ' + date.getFullYear();
            //period['id'] = 'Quarterly_' + period['startDate'];
            period['iso'] = year + 'Q' + quarter;
            period['id'] = period['iso'];
            periods.push(period);
            date.setDate( 0 );
            quarter--;
        }

        return rev(periods);
    };
}

function SixMonthlyPeriodType( monthNames )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;

        var period = {};
        period['startDate'] = year + '-01-01';
        period['endDate'] = year + '-06-30';
        period['name'] = monthNames[0] + ' - ' + monthNames[5] + ' ' + year;
        //period['id'] = 'SixMonthly_' + period['startDate'];
        period['iso'] = year + 'S1';
		period['id'] = period['iso'];
        periods.push(period);

        period = {};
        period['startDate'] = year + '-07-01';
        period['endDate'] = year + '-12-31';
        period['name'] = monthNames[6] + ' - ' + monthNames[11] + ' ' + year;
        //period['id'] = 'SixMonthly_' + period['startDate'];
        period['iso'] = year + 'S2';
		period['id'] = period['iso'];
        periods.push(period);

        return periods;
    };
}

function YearlyPeriodType( format_yyyymmdd, rev )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '31 Dec ' + year );

        while ( ( year - date.getFullYear() ) < 10 )
        {
            var period = {};
            period['endDate'] = format_yyyymmdd( date );
            date.setMonth( 0, 1 );
            period['startDate'] = format_yyyymmdd( date );
            period['name'] = date.getFullYear();
            //period['id'] = 'Yearly_' + period['startDate'];
            period['iso'] = date.getFullYear();
            period['id'] = period['iso'];
            periods.push(period);
            date.setDate(0);
        }

        return rev( periods );
    };
}

function FinancialOctoberPeriodType( format_yyyymmdd, monthNames, rev )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '30 Sep ' + ( year + 1 ) );
        
        for ( var i = 0; i < 10; i++ )
        {
			var period = {};
			period['endDate'] = format_yyyymmdd( date );
			date.setYear( date.getFullYear() - 1 );
			date.setDate( date.getDate() + 1 );
			period['startDate'] = format_yyyymmdd( date );
			period['name'] = monthNames[9] + ' ' + date.getFullYear() + ' - ' + monthNames[8] + ' ' + ( date.getFullYear() + 1 );
			period['iso'] = date.getFullYear() + 'Oct';
			period['id'] = period['iso'];
			periods.push( period );
			date.setDate( date.getDate() - 1 );
		}
		
		return rev( periods );
    };
}

function FinancialJulyPeriodType( format_yyyymmdd, monthNames, rev )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '30 Jun ' + ( year + 1 ) );
        
        for ( var i = 0; i < 10; i++ )
        {
			var period = {};
			period['endDate'] = format_yyyymmdd( date );
			date.setYear( date.getFullYear() - 1 );
			date.setDate( date.getDate() + 1 );
			period['startDate'] = format_yyyymmdd( date );
			period['name'] = monthNames[6] + ' ' + date.getFullYear() + ' - ' + monthNames[5] + ' ' + ( date.getFullYear() + 1 );
			period['iso'] = date.getFullYear() + 'July';
			period['id'] = period['iso'];
			periods.push( period );
			date.setDate( date.getDate() - 1 );
		}
		
		return rev( periods );
    };
}

function FinancialAprilPeriodType( format_yyyymmdd, monthNames, rev )
{
    this.generatePeriods = function( offset )
    {
        var periods = [];
        var year = new Date().getFullYear() + offset;
        var date = new Date( '31 Mar ' + ( year + 1 ) );
        
        for ( var i = 0; i < 10; i++ )
        {
			var period = {};
			period['endDate'] = format_yyyymmdd( date );
			date.setYear( date.getFullYear() - 1 );
			date.setDate( date.getDate() + 1 );
			period['startDate'] = format_yyyymmdd( date );
			period['name'] = monthNames[3] + ' ' + date.getFullYear() + ' - ' + monthNames[2] + ' ' + ( date.getFullYear() + 1 );
			period['iso'] = date.getFullYear() + 'April';
			period['id'] = period['iso'];
			periods.push( period );
			date.setDate( date.getDate() - 1 );
		}
		
		return rev( periods );
    };
}
