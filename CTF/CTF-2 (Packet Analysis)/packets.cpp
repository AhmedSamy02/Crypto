#include <iostream>
using namespace std;
int main(int argc, char const *argv[])
{
    string x;
    x = "Gur synt vf cvpbPGS{c33xno00_1_f33_h_qrnqorrs}";
    for (int i = 0; i < x.size(); i++)
    {
        if (x[i] >= 'a' && x[i] <= 'z')
        {
            x[i] = 'a' + (x[i] - 'a' + 13) % 26;
        }
        else if (x[i] >= 'A' && x[i] <= 'Z')
        {
            x[i] = 'A' + (x[i] - 'A' + 13) % 26;
        }
    }
    cout<<x<<endl;
    return 0;
}
