# ---------- excel write -----------
use Excel::Writer::XLSX;

$file = "ssssss.xlsx";
my $workbook  = Excel::Writer::XLSX->new($file);
my $worksheet = $workbook->add_worksheet();

my @eec =  (
               ['maggie', 'milly', 'molly', 'may'  ],
               [13,       14,      15,      16     ],
               ['shell',  'star',  'crab',  'stone']
           );

$worksheet->write_col( 'A1', \@eec );
$workbook->close();
