#!/usr/bin/perl

use Spreadsheet::XLSX;	 #excel read
use Excel::Writer::XLSX; #excel write
use utf8;

binmode(STDOUT, ':encoding(cp949)');
binmode(STDIN, ':encoding(cp949)');

use Encode qw(decode encode);

my $file = "test.xlsx";
my $file_out = "test_out.xlsx";

#---------------------
# main
#---------------------
#&test1();
#&test2();
&test3();
sub test3
{
	my $val; 
	my $val_decode1;
	my $val_decode2;
	my $excel = Spreadsheet::XLSX->new('F5 납품장비 시리얼.xlsx') or die &die_handle("excel read");

	for my $sheet(@{$excel->{Worksheet}})
	{
		foreach my $row ($sheet->{MinRow}..$sheet->{MaxRow})
		{
			printf "row %d\n", $row;
			foreach my $col ($sheet->{MinCol}..$sheet->{MaxCol})
			{
#				$val = $sheet->{Cells}[$row][$col]->{Val};
#				printf "-- val = %s\n", $val;
				$val_decode1 = decode('utf8', $sheet->{Cells}[$row][$col]->{Val});
				printf "-- val_decode1 = %s\n", $val_decode1;
			}
			if($row >= 6)
			{
				last;
			}
		}
	}
}

sub test2
{
	my $val; 
	my $val_decode1;
	my $val_decode2;
	my $excel = Spreadsheet::XLSX->new($file) or die &die_handle("excel read");

	my $workbook  = Excel::Writer::XLSX->new($file_out);
	my $worksheet = $workbook->add_worksheet('result5');
		
	for my $sheet(@{$excel->{Worksheet}})
	{
		foreach my $row ($sheet->{MinRow}..$sheet->{MaxRow})
		{
			foreach my $col ($sheet->{MinCol}..$sheet->{MaxCol})
			{
				printf "row %d, col = %d\n", $row, $col;
				$val_decode1 = decode('utf8', $sheet->{Cells}[$row][$col]->{Val});
				printf "-- val_decode1 = %s\n", $val_decode1;
				
				$worksheet->write($row, $col, $val_decode1);
				$worksheet->write($row, 5, $val_decode1);
				$worksheet->write($row, 6, $val_decode1);
			}
		}
	}
	$workbook->close();
}
sub test1
{
	my $val; 
	my $val_decode;
	my $excel = Spreadsheet::XLSX->new($file) or die &die_handle("excel read");
	for my $sheet(@{$excel->{Worksheet}})
	{
		foreach my $row ($sheet->{MinRow}..$sheet->{MaxRow})
		{
			foreach my $col ($sheet->{MinCol}..$sheet->{MaxCol})
			{
				printf "row %d, col = %d\n", $row, $col;
				$val = $sheet->{Cells}[$row][$col]->{Val};
				printf "-- val = %s\n", $val;
				$val_decode = decode('utf8', $sheet->{Cells}[$row][$col]->{Val});
				printf "-- val_decode = %s\n", $val_decode;
				my $val_decode2 = Encode::from_to($sheet->{Cells}[$row][$col]->{Val},'utf16LE', 'utf8');
				printf "-- val_decode = %s\n", $val_decode;
				printf "-- val_decode = %s\n", $val_decode2;
			}
		}
	}
}
sub die_handle
{
	my $title = shift;
	if(defined $!) #eval error 이전에 system error를 먼저 처리해야 한다.
	{
		printf STDOUT "message:: $title: $!\n";
	}
	elsif(defined $@)
	{
		if(ref $@)
		{
    		printf STDOUT "message:: $title: $@->{message}\n";
    	}
		else
		{
			printf STDOUT "message:: $title: $@\n";
    	}
	}
	else
	{
		printf STDOUT "message:: $title: no detail.\n";
	}
}