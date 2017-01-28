package xyz.mkotb.summary;

import org.apache.commons.codec.binary.Base64;

import java.util.List;

public class TextTest {
    public static void main(String[] args) {
        Summary summary = new Summary();
        List<String> result;
        long before = System.currentTimeMillis();

        result = summary.summaryFor(INPUT, 4, true);

        long after = System.currentTimeMillis();

        System.out.println("Wrote summary in " + (after - before) + " ms!\n");
        TestUtils.printSummary(result);
    }

    public static final String INPUT = new String(Base64.decodeBase64(
            "RG9uYWxkIFRydW1wIGlzIGZhY2luZyBhIHByZS1lbXB0aXZlIGJhY2" +
                    "tsYXNoIG92ZXIgaGlzIHJlcG9ydGVkIHBsYW5zIGZvciBh" +
                    "IGJhbiBvbiBwZW9wbGUgZnJvbSBzZXZlbiBNdXNsaW0tbW" +
                    "Fqb3JpdHkgY291bnRyaWVzIGVudGVyaW5nIHRoZSBVUy4N" +
                    "Cg0KVGhlIFByZXNpZGVudCBpcyB3aWRlbHkgcmVwb3J0ZW" +
                    "QgdG8gYmUgcGxhbm5pbmcgYSBzZXJpZXMgb2YgZXhlY3V0" +
                    "aXZlIG9yZGVycyBhbmQgcG9saWN5IGFubm91bmNlbWVudH" +
                    "Mgb24gV2VkbmVzZGF5LCB3aGljaCBoZSBoYXMgYmlsbGVk" +
                    "IGFzIGEg4oCcYmlnIGRheSBwbGFubmVkIG9uIG5hdGlvbm" +
                    "FsIHNlY3VyaXR54oCdLg0KDQpSZXV0ZXJzIHJlcG9ydGVk" +
                    "IHRoYXQgdGhlIG9yZGVycyBjb3VsZCBpbmNsdWRlIGEgdG" +
                    "VtcG9yYXJ5IGJhbiBvbiBtb3N0IHJlZnVnZWVzIGFuZCBh" +
                    "IGJsb2NrIG9uIGFueSB2aXNhcyBhdCBhbGwgYmVpbmcgaX" +
                    "NzdWVkIGZvciBwZW9wbGUgZnJvbSBTeXJpYSwgSXJhcSwg" +
                    "SXJhbiwgTGlieWEsIFNvbWFsaWEsIFN1ZGFuIGFuZCBZZW" +
                    "1lbi4gSXQgY2l0ZWQgY29uZ3Jlc3Npb25hbCBhaWRlcyBh" +
                    "bmQgaW1taWdyYXRpb24gZXhwZXJ0cyB3aG8gaGFkIGJlZW" +
                    "4gYnJpZWZlZCBvbiB0aGUgbWF0dGVyLCB3aG8gYXNrZWQg" +
                    "bm90IHRvIGJlIGlkZW50aWZpZWQuDQoNClRoZSBVTiBsZW" +
                    "QgY3JpdGljaXNtIG9mIHRoZSByZXBvcnRlZCBwbGFucywg" +
                    "c2F5aW5nIGV2ZW4gYSB0ZW1wb3JhcnkgZGVsYXkgb24gZX" +
                    "hpc3RpbmcgcmVmdWdlZSByZWxvY2F0aW9uIHByb2dyYW1t" +
                    "ZXMgd291bGQgYmUg4oCcaGlnaGx5IGNvbmNlcm5pbmfigJ" +
                    "0gZm9yIHRob3NlIGludm9sdmVkLiBBbmQgbGVnYWwgZXhw" +
                    "ZXJ0cyBzdWdnZXN0ZWQgdGhlIGJhbiBvbiBwZW9wbGUgZn" +
                    "JvbSBNdXNsaW0tbWFqb3JpdHkgY291bnRyaWVzIGNvdWxk" +
                    "IGJlIGNoYWxsZW5nZWQgYXMgdW5jb25zdGl0dXRpb25hbC" +
                    "BpZiBpdCB3YXMgcHJvdmVkIHRvIGRpc2NyaW1pbmF0ZSBv" +
                    "biByZWxpZ2lvdXMgZ3JvdW5kcy4NCg0K4oCcQW55IHN1Yn" +
                    "N0YW50aWFsIGRlbGF5IGluIHRoZSByZWxvY2F0aW9uIG9m" +
                    "IHJlZnVnZWVzLi4ud291bGQgYmUgaGlnaGx5IGNvbmNlcm" +
                    "5pbmcgZnJvbSBhIGh1bWFuaXRhcmlhbiBwZXJzcGVjdGl2" +
                    "ZSzigJ0gc2FpZCBDYXRoZXJpbmUgU3R1YmJlcmZpZWxkLC" +
                    "BhIHNwb2tlc3dvbWFuIGZvciB0aGUgVU4gSGlnaCBDb21t" +
                    "aXNzaW9uZXIgZm9yIFJlZnVnZWVzLg0KDQrigJxUaGVzZS" +
                    "BtZW4sIHdvbWVuIGFuZCBjaGlsZHJlbiBjYW4gbm8gbG9u" +
                    "Z2VyIGFmZm9yZCB0byB3YWl0LuKAnQ0KDQpIaXJvc2hpIE" +
                    "1vdG9tdXJhLCBhbiBpbW1pZ3JhdGlvbiBleHBlcnQgYXQg" +
                    "dGhlIFVDTEEgU2Nob29sIG9mIExhdywgc2FpZCBkZXRyYW" +
                    "N0b3JzIGNvdWxkIGxhdW5jaCBsZWdhbCBjaGFsbGVuZ2Vz" +
                    "IGlmIHRoZSB2aXNhIGJhbiBkb2VzIGluZGVlZCB0YXJnZX" +
                    "Qgb25seSBNdXNsaW0tbWFqb3JpdHkgY291bnRyaWVzLg0K" +
                    "DQpMZWdhbCBhcmd1bWVudHMgY291bGQgY2xhaW0gdGhlIG" +
                    "V4ZWN1dGl2ZSBvcmRlcnMgZGlzY3JpbWluYXRlIGFnYWlu" +
                    "c3QgYSBwYXJ0aWN1bGFyIHJlbGlnaW9uLCB3aGljaCB3b3" +
                    "VsZCBiZSB1bmNvbnN0aXR1dGlvbmFsLCBoZSBzYWlkLg0K" +
                    "DQrigJxIaXMgY29tbWVudHMgZHVyaW5nIHRoZSBjYW1wYW" +
                    "lnbiBhbmQgYSBudW1iZXIgb2YgcGVvcGxlIG9uIGhpcyB0" +
                    "ZWFtIGZvY3VzZWQgdmVyeSBtdWNoIG9uIHJlbGlnaW9uIG" +
                    "FzIHRoZSB0YXJnZXQs4oCdIE1yIE1vdG9tdXJhIHNhaWQu" +
                    "DQoNClR3ZWV0aW5nIGluIHJlYWN0aW9uIHRvIGVhcmx5IG" +
                    "1lZGlhIHJlcG9ydHMgbGFzdCBuaWdodCwgdGhlIGRpcmVj" +
                    "dG9yIG9mIHRoZSBDb3VuY2lsIG9uIEFtZXJpY2FuLUlzbG" +
                    "FtaWMgUmVsYXRpb25zIChDQUlSKSBOaWhhZCBBd2FkIHNh" +
                    "aWQ6IOKAnFRoZXNlIFtleGVjdXRpdmUgb3JkZXJzXSB3aW" +
                    "xsIG5vdCBtYWtlIG91ciBuYXRpb24gc2FmZXIsIHJhdGhl" +
                    "ciB0aGV5IHdpbGwgbWFrZSBpdCBtb3JlIGZlYXJmdWwgYW" +
                    "5kIGxlc3Mgd2VsY29taW5nLuKAnQ0KDQpBbmQgU3RlcGhl" +
                    "biBMZWdvbXNreSwgd2hvIHdhcyBjaGllZiBjb3Vuc2VsIG" +
                    "F0IFVTIENpdGl6ZW5zaGlwIGFuZCBJbW1pZ3JhdGlvbiBT" +
                    "ZXJ2aWNlcyBpbiB0aGUgT2JhbWEgYWRtaW5pc3RyYXRpb2" +
                    "4sIHNhaWQgdGhlIFByZXNpZGVudCBoYWQgdGhlIGF1dGhv" +
                    "cml0eSB0byBsaW1pdCByZWZ1Z2VlIGFkbWlzc2lvbnMgYW" +
                    "5kIHRoZSBpc3N1YW5jZSBvZiB2aXNhcyB0byBzcGVjaWZp" +
                    "YyBjb3VudHJpZXMgaWYgdGhlIGFkbWluaXN0cmF0aW9uIG" +
                    "RldGVybWluZWQgaXQgd2FzIGluIHRoZSBwdWJsaWMgaW50" +
                    "ZXJlc3QuDQoNCuKAnEZyb20gYSBsZWdhbCBzdGFuZHBvaW" +
                    "50LCBpdCB3b3VsZCBiZSBleGFjdGx5IHdpdGhpbiBoaXMg" +
                    "bGVnYWwgcmlnaHRzLOKAnSBzYWlkIE1yIExlZ29tc2t5LC" +
                    "BhIHByb2Zlc3NvciBhdCBXYXNoaW5ndG9uIFVuaXZlcnNp" +
                    "dHkgU2Nob29sIG9mIExhdyBpbiBTdCBMb3Vpcy4g4oCcQn" +
                    "V0IGZyb20gYSBwb2xpY3kgc3RhbmRwb2ludCwgaXQgd291" +
                    "bGQgYmUgdGVycmlibGUgaWRlYSBiZWNhdXNlIHRoZXJlIG" +
                    "lzIHN1Y2ggYW4gdXJnZW50IGh1bWFuaXRhcmlhbiBuZWVk" +
                    "IHJpZ2h0IG5vdyBmb3IgcmVmdWdlZXMu4oCdDQoNCldoaW" +
                    "xlIGl0IHJlbWFpbnMgdG8gYmUgc2VlbiBwcmVjaXNlbHkg" +
                    "d2hhdCBNciBUcnVtcCB3aWxsIGFubm91bmNlIG9uIFdlZG" +
                    "5lc2RheSwgc29tZSBwbGFucyBmb3IgaGlzIHNpeHRoIGRh" +
                    "eSBpbiBvZmZpY2UgYXJlIG1vcmUgY2VydGFpbi4NCg0KSG" +
                    "UgaXMgZXhwZWN0ZWQgdG8gdGFrZSBwYXJ0IGluIGEgY2Vy" +
                    "ZW1vbnkgaW5zdGFsbGluZyB0aGUgcmV0aXJlZCBNYXJpbm" +
                    "UgR2VuZXJhbCBKb2huIEtlbGx5IGFzIGhpcyBuZXcgc2Vj" +
                    "cmV0YXJ5IG9mIGhvbWVsYW5kIHNlY3VyaXR5Lg0KDQpBbm" +
                    "QgYWNjb3JkaW5nIHRvIG9uZSBvZiBNciBUcnVtcOKAmXMg" +
                    "bGF0ZSBuaWdodCB0d2VldHMgb24gVHVlc2RheSwgaGUgd2" +
                    "lsbCBtYWtlIGdvb2Qgb24gaGlzIHByb21pc2UgdG8gb3Jk" +
                    "ZXIgdGhlIGJ1aWxkaW5nIG9mIGEgYm9yZGVyIHdhbGwgYm" +
                    "V0d2VlbiB0aGUgVVMgYW5kIE1leGljby4NCg0KV2hpdGUg" +
                    "SG91c2Ugc3Bva2VzbWFuIFNlYW4gU3BpY2VyIHNhaWQgb2" +
                    "4gVHVlc2RheSB0aGF0IHRoZSBTdGF0ZSBhbmQgSG9tZWxh" +
                    "bmQgU2VjdXJpdHkgRGVwYXJ0bWVudHMgd291bGQgd29yay" +
                    "BvbiB3aGF0IE1yIFRydW1wIGhhcyBiaWxsZWQgYXMgYW4g" +
                    "4oCcZXh0cmVtZSB2ZXR0aW5n4oCdIHByb2Nlc3Mgb25jZS" +
                    "BUcnVtcCBub21pbmVlIFJleCBUaWxsZXJzb24gaGFzIGJl" +
                    "ZW4gaW5zdGFsbGVkIGFzIFNlY3JldGFyeSBvZiBTdGF0ZS" +
                    "4NCg0KU291cmNlcyB0b2xkIFJldXRlcnMgdGhlIE1leGlj" +
                    "byB3YWxsIGFuZCBpbW1pZ3JhdGlvbiBvcmRlcnMgd2VyZS" +
                    "BsaWtlbHkgdG8gYmUgdGhlIGZpcnN0IHNpZ25lZCBvbiBX" +
                    "ZWRuZXNkYXksIHdoaWxlIHRoZSByZWZ1Z2VlIGJhbiB3aW" +
                    "xsIGNvbWUgYWZ0ZXJ3YXJkcywgcG9zc2libHkgbGF0ZXIg" +
                    "aW4gdGhlIHdlZWsuDQoNClRoZSBsYXR0ZXIgdGhyZWF0ZW" +
                    "5zIGEgcmVmdWdlZSByZXNldHRsZW1lbnQgZGVhbCB3aXRo" +
                    "IEF1c3RyYWxpYSBzaWduZWQgbGF0ZSBsYXN0IHllYXIsIG" +
                    "FuZCBjb3VsZCBsZWF2ZSBtb3JlIHRoYW4gMSwwMDAgYXN5" +
                    "bHVtIHNlZWtlcnMgaW4gbGltYm8uDQoNClRoZSBVUyBhZ3" +
                    "JlZWQgdG8gcmVzZXR0bGUgYW4gdW5zcGVjaWZpZWQgbnVt" +
                    "YmVyIG9mIHJlZnVnZWVzIGJlaW5nIGhlbGQgaW4gUGFwdW" +
                    "EgTmV3IEd1aW5lYSAoUE5HKSBhbmQgdGhlIHRpbnkgU291" +
                    "dGggUGFjaWZpYyBpc2xhbmQgbmF0aW9uIG9mIE5hdXJ1IG" +
                    "9uIEF1c3RyYWxpYeKAmXMgYmVoYWxmLCB1bmRlciBhIGRl" +
                    "YWwgdG8gYmUgYWRtaW5pc3RlcmVkIGJ5IHRoZSBVLk4uIH" +
                    "JlZnVnZWUgYWdlbmN5Lg0KDQpUaGF0IGNhbWUgYWZ0ZXIg" +
                    "QXVzdHJhbGlhIGFncmVlZCB0byBoZWxwIHJlc2V0dGxlIH" +
                    "JlZnVnZWVzIGZyb20gR3VhdGVtYWxhLCBIb25kdXJhcyBh" +
                    "bmQgRWwgU2FsdmFkb3IgdW5kZXIgYSBVUy1sZWQgcHJvZ3" +
                    "JhbW1lLg0KDQpBdXN0cmFsaWEncyB0b3VnaCBib3JkZXIg" +
                    "c2VjdXJpdHkgbGF3cyBtYW5kYXRlIHRoYXQgYXN5bHVtIH" +
                    "NlZWtlcnMgaW50ZXJjZXB0ZWQgdHJ5aW5nIHRvIHJlYWNo" +
                    "IHRoZSBjb3VudHJ5IGJ5IGJvYXQgZ28gZm9yIHByb2Nlc3" +
                    "NpbmcgdG8gZGV0ZW50aW9uIGNhbXBzIG9uIFBORydzIE1h" +
                    "bnVzIGlzbGFuZCBhbmQgTmF1cnUuDQoNCiJXZSBhbHJlYW" +
                    "R5IGRpZG4ndCBoYXZlIG11Y2ggaG9wZSB0aGUgVVMgd291" +
                    "bGQgYWNjZXB0IHVzLCIgc2FpZCBCZWhyb3V6IEJvb2NoYW" +
                    "5pLCBhbiBJcmFuaWFuIHJlZnVnZWUgd2hvIGhhcyBzcGVu" +
                    "dCBtb3JlIHRoYW4gdGhyZWUgeWVhcnMgb24gTWFudXMgaX" +
                    "NsYW5kLg0KDQoiSWYgdGhleSBkbyBub3QgdGFrZSB1cywg" +
                    "QXVzdHJhbGlhIHdpbGwgaGF2ZSB0by4i"
    ));
}
