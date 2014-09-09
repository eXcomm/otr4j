package net.java.otr4j.session;

import java.io.IOException;
import java.util.regex.Pattern;

import net.java.otr4j.OtrPolicy;

import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests for OTR Fragmenter.
 *
 * @author Danny van Heumen
 */
public class OtrFragmenterTest {

	private static final OtrPolicy POLICY_V23 = new OtrPolicy() {

		public boolean getAllowV1() {
			return false;
		}

		public boolean getAllowV2() {
			return true;
		}

		public boolean getAllowV3() {
			return true;
		}

		public boolean getRequireEncryption() {
			return false;
		}

		public boolean getSendWhitespaceTag() {
			return false;
		}

		public boolean getWhitespaceStartAKE() {
			return false;
		}

		public boolean getErrorStartAKE() {
			return false;
		}

		public int getPolicy() {
			return 0;
		}

		public boolean getEnableAlways() {
			return true;
		}

		public boolean getEnableManual() {
			return false;
		}
		
		public void setAllowV1(boolean value) {}
		public void setAllowV2(boolean value) {}
		public void setAllowV3(boolean value) {}
		public void setRequireEncryption(boolean value) {}
		public void setSendWhitespaceTag(boolean value) {}
		public void setWhitespaceStartAKE(boolean value) {}
		public void setErrorStartAKE(boolean value) {}
		public void setEnableAlways(boolean value) {}
		public void setEnableManual(boolean value) {}
	};

	private static final OtrPolicy POLICY_V3 = new OtrPolicy() {

		public boolean getAllowV1() {
			return false;
		}

		public boolean getAllowV2() {
			return false;
		}

		public boolean getAllowV3() {
			return true;
		}

		public boolean getRequireEncryption() {
			return false;
		}

		public boolean getSendWhitespaceTag() {
			return false;
		}

		public boolean getWhitespaceStartAKE() {
			return false;
		}

		public boolean getErrorStartAKE() {
			return false;
		}

		public int getPolicy() {
			return 0;
		}

		public boolean getEnableAlways() {
			return true;
		}

		public boolean getEnableManual() {
			return false;
		}
		
		public void setAllowV1(boolean value) {}
		public void setAllowV2(boolean value) {}
		public void setAllowV3(boolean value) {}
		public void setRequireEncryption(boolean value) {}
		public void setSendWhitespaceTag(boolean value) {}
		public void setWhitespaceStartAKE(boolean value) {}
		public void setErrorStartAKE(boolean value) {}
		public void setEnableAlways(boolean value) {}
		public void setEnableManual(boolean value) {}
	};

	private static final OtrPolicy POLICY_V2 = new OtrPolicy() {

		public boolean getAllowV1() {
			return false;
		}

		public boolean getAllowV2() {
			return true;
		}

		public boolean getAllowV3() {
			return false;
		}

		public boolean getRequireEncryption() {
			return false;
		}

		public boolean getSendWhitespaceTag() {
			return false;
		}

		public boolean getWhitespaceStartAKE() {
			return false;
		}

		public boolean getErrorStartAKE() {
			return false;
		}

		public int getPolicy() {
			return 0;
		}

		public boolean getEnableAlways() {
			return true;
		}

		public boolean getEnableManual() {
			return false;
		}
		
		public void setAllowV1(boolean value) {}
		public void setAllowV2(boolean value) {}
		public void setAllowV3(boolean value) {}
		public void setRequireEncryption(boolean value) {}
		public void setSendWhitespaceTag(boolean value) {}
		public void setWhitespaceStartAKE(boolean value) {}
		public void setErrorStartAKE(boolean value) {}
		public void setEnableAlways(boolean value) {}
		public void setEnableManual(boolean value) {}
	};

	private static final OtrPolicy POLICY_V1 = new OtrPolicy() {

		public boolean getAllowV1() {
			return true;
		}

		public boolean getAllowV2() {
			return false;
		}

		public boolean getAllowV3() {
			return false;
		}

		public boolean getRequireEncryption() {
			return false;
		}

		public boolean getSendWhitespaceTag() {
			return false;
		}

		public boolean getWhitespaceStartAKE() {
			return false;
		}

		public boolean getErrorStartAKE() {
			return false;
		}

		public int getPolicy() {
			return 0;
		}

		public boolean getEnableAlways() {
			return true;
		}

		public boolean getEnableManual() {
			return false;
		}
		
		public void setAllowV1(boolean value) {}
		public void setAllowV2(boolean value) {}
		public void setAllowV3(boolean value) {}
		public void setRequireEncryption(boolean value) {}
		public void setSendWhitespaceTag(boolean value) {}
		public void setWhitespaceStartAKE(boolean value) {}
		public void setErrorStartAKE(boolean value) {}
		public void setEnableAlways(boolean value) {}
		public void setEnableManual(boolean value) {}
	};

	private static final String specV3MessageFull = "?OTR:AAMDJ+MVmSfjFZcAAAAAAQAAAAIAAADA1g5IjD1ZGLDVQEyCgCyn9hbrL3KAbGDdzE2ZkMyTKl7XfkSxh8YJnudstiB74i4BzT0W2haClg6dMary/jo9sMudwmUdlnKpIGEKXWdvJKT+hQ26h9nzMgEditLB8vjPEWAJ6gBXvZrY6ZQrx3gb4v0UaSMOMiR5sB7Eaulb2Yc6RmRnnlxgUUC2alosg4WIeFN951PLjScajVba6dqlDi+q1H5tPvI5SWMN7PCBWIJ41+WvF+5IAZzQZYgNaVLbAAAAAAAAAAEAAAAHwNiIi5Ms+4PsY/L2ipkTtquknfx6HodLvk3RAAAAAA==.";

	private static final String[] specV3MessageParts199 = new String[] {
		"?OTR|5a73a599|27e31597,00001,00003,?OTR:AAMDJ+MVmSfjFZcAAAAAAQAAAAIAAADA1g5IjD1ZGLDVQEyCgCyn9hbrL3KAbGDdzE2ZkMyTKl7XfkSxh8YJnudstiB74i4BzT0W2haClg6dMary/jo9sMudwmUdlnKpIGEKXWdvJKT+hQ26h9nzMgEditLB8v,",
		"?OTR|5a73a599|27e31597,00002,00003,jPEWAJ6gBXvZrY6ZQrx3gb4v0UaSMOMiR5sB7Eaulb2Yc6RmRnnlxgUUC2alosg4WIeFN951PLjScajVba6dqlDi+q1H5tPvI5SWMN7PCBWIJ41+WvF+5IAZzQZYgNaVLbAAAAAAAAAAEAAAAHwNiIi5Ms+4PsY/L2i,",
		"?OTR|5a73a599|27e31597,00003,00003,pkTtquknfx6HodLvk3RAAAAAA==.,"
	};

	private static final String specV2MessageFull = "?OTR:AAEDAAAAAQAAAAEAAADAVf3Ei72ZgFeKqWvLMnuVPVCwxktsOZ1QdjeLp6jn62mCVtlY9nS6sRkecpjuLYHRxyTdRu2iEVtSsjZqK55ovZ35SfkOPHeFYa9BIuxWi9djHMVKQ8KOVGAVLibjZ6P8LreDSKtWDv9YQjIEnkwFVGCPfpBq2SX4VTQfJAQXHggR8izKxPvluXUdG9rIPh4cac98++VLdIuFMiEXjUIoTX2rEzunaCLMy0VIfowlRsgsKGrwhCCv7hBWyglbzwz+AAAAAAAAAAQAAAF2SOrJvPUerB9mtf4bqQDFthfoz/XepysnYuReHHEXKe+BFkaEoMNGiBl4TCLZx72DvmZwKCewWRH1+W66ggrXKw2VdVl+vLsmzxNyWChGLfBTL5/3SUF09BfmCEl03Ckk7htAgyAQcBf90RJznZndv7HwVAi3syupi0sQDdOKNPyObR5FRtqyqudttWmSdmGCGFcZ/fZqxQNsHB8QuYaBiGL7CDusES+wwfn8Q7BGtoJzOPDDx6KyIyox/flPx2DZDJIZrMz9b0V70a9kqKLo/wcGhvHO6coCyMxenBAacLJ1DiINLKoYOoJTM7zcxsGnvCxaDZCvsmjx3j8Yc5r3i3ylllCQH2/lpr/xCvXFarGtG7+wts+UqstS9SThLBQ9Ojq4oPsX7HBHKvq19XU3/ChIgWMy+bczc5gpkC/eLAIGfJ0D5DJsl68vMXSmCoFK0HTwzzNa7lnZK4IutYPBNBCv0pWORQqDpskEz96YOGyB8+gtpFgCrkuV1bSB9SRVmEBfDtKPQFhKowAAAAA=.";

	private static final String[] specV2MessageParts318 = new String[] {
		"?OTR,1,3,?OTR:AAEDAAAAAQAAAAEAAADAVf3Ei72ZgFeKqWvLMnuVPVCwxktsOZ1QdjeLp6jn62mCVtlY9nS6sRkecpjuLYHRxyTdRu2iEVtSsjZqK55ovZ35SfkOPHeFYa9BIuxWi9djHMVKQ8KOVGAVLibjZ6P8LreDSKtWDv9YQjIEnkwFVGCPfpBq2SX4VTQfJAQXHggR8izKxPvluXUdG9rIPh4cac98++VLdIuFMiEXjUIoTX2rEzunaCLMy0VIfowlRsgsKGrwhCCv7hBWyglbzwz+AAAAAAAAAAQAAAF2SOr,",
		"?OTR,2,3,JvPUerB9mtf4bqQDFthfoz/XepysnYuReHHEXKe+BFkaEoMNGiBl4TCLZx72DvmZwKCewWRH1+W66ggrXKw2VdVl+vLsmzxNyWChGLfBTL5/3SUF09BfmCEl03Ckk7htAgyAQcBf90RJznZndv7HwVAi3syupi0sQDdOKNPyObR5FRtqyqudttWmSdmGCGFcZ/fZqxQNsHB8QuYaBiGL7CDusES+wwfn8Q7BGtoJzOPDDx6KyIyox/flPx2DZDJIZrMz9b0V70a9kqKLo/wcGhvHO6coCyMxenBAacLJ1DiI,",
		"?OTR,3,3,NLKoYOoJTM7zcxsGnvCxaDZCvsmjx3j8Yc5r3i3ylllCQH2/lpr/xCvXFarGtG7+wts+UqstS9SThLBQ9Ojq4oPsX7HBHKvq19XU3/ChIgWMy+bczc5gpkC/eLAIGfJ0D5DJsl68vMXSmCoFK0HTwzzNa7lnZK4IutYPBNBCv0pWORQqDpskEz96YOGyB8+gtpFgCrkuV1bSB9SRVmEBfDtKPQFhKowAAAAA=.,"
	};

	@Test(expected = NullPointerException.class)
	public void testNullPolicyConstruction() {
		FragmenterInstructions instructions = new FragmenterInstructions(2, 100);
		new OtrFragmenter(null, instructions);
	}

	@Test
	public void testNullInstructionsConstructionSetsUnlimited() {
		Session session = createSessionMock(null, 0, 0);
		OtrFragmenter fragmenter = new OtrFragmenter(session, null);
		Assert.assertEquals(FragmenterInstructions.UNLIMITED,
				fragmenter.getInstructions().maxFragmentsAllowed);
		Assert.assertEquals(FragmenterInstructions.UNLIMITED,
				fragmenter.getInstructions().maxFragmentSize);
	}

	@Test
	public void testConstruction() {
		Session session = createSessionMock(null, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(2, 100);
		new OtrFragmenter(session, instructions);
	}
	
	@Test
	public void testGetInstructions() {
		Session session = createSessionMock(null, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(2, 100);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		Assert.assertSame(instructions, fragmenter.getInstructions());
	}

	@Test
	public void testUnlimitedSizedFragmentToSingleMessage() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, -1);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV3MessageFull);
		Assert.assertArrayEquals(new String[] {specV3MessageFull}, msg);
	}

	@Test
	public void testUnlimitedSizedFragmentToSingleMessageV2() throws IOException {
		Session session = createSessionMock(POLICY_V2, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, -1);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV2MessageFull);
		Assert.assertArrayEquals(new String[] {specV2MessageFull}, msg);
	}

	@Test
	public void testLargeEnoughFragmentToSingleMessage() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 354);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV3MessageFull);
		Assert.assertArrayEquals(new String[] {specV3MessageFull}, msg);
	}

	@Test
	public void testLargeEnoughFragmentToSingleMessageV2() throws IOException {
		Session session = createSessionMock(POLICY_V2, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 830);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV2MessageFull);
		Assert.assertArrayEquals(new String[] {specV2MessageFull}, msg);
	}

	@Test
	public void testCalculateNumberOfFragmentsUnlimitedSize() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, -1);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		int num = fragmenter.numberOfFragments(specV3MessageFull);
		Assert.assertEquals(1, num);
	}

	@Test
	public void testCalculateNumberOfFragmentsLargeEnoughSize() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 1000);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		int num = fragmenter.numberOfFragments(specV3MessageFull);
		Assert.assertEquals(1, num);
	}

	@Test
	public void testCalculateNumberOfFragmentsNumFragmentsSmallFragmentSize() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);;
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 199);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		int num = fragmenter.numberOfFragments(specV3MessageFull);
		Assert.assertEquals(3, num);
	}

	@Test
	public void testCalculateNumberOfFragmentsNumFragmentsSmallFragmentSize2() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);;
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 80);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		int num = fragmenter.numberOfFragments(specV3MessageFull);
		Assert.assertEquals(9, num);
	}

	@Test(expected = IOException.class)
	public void testFragmentSizeTooSmallForOverhead() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);;
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 35);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.numberOfFragments(specV3MessageFull);
	}
	
	@Test(expected = IOException.class)
	public void testFragmentSizeTooSmallForPayload() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1,
				OtrFragmenter.computeHeaderV3Size());
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.numberOfFragments(specV3MessageFull);
	}

	@Test(expected = IOException.class)
	public void testNumFragmentsTooLimited() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(2, 100);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.fragment(specV3MessageFull);
	}

	@Test
	public void testV3MessageToSplit() throws IOException {
		Session session = createSessionMock(POLICY_V3, 0x5a73a599, 0x27e31597);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 199);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV3MessageFull);
		Assert.assertArrayEquals(specV3MessageParts199, msg);
	}

	@Test
	public void testV2MessageToSplit() throws IOException {
		Session session = createSessionMock(POLICY_V2, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 318);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV2MessageFull);
		Assert.assertArrayEquals(specV2MessageParts318, msg);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testV1ComputeHeaderSize() throws IOException {
		Session session = createSessionMock(POLICY_V1, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 310);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.numberOfFragments(specV2MessageFull);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testV1MessageToSplit() throws IOException {
		Session session = createSessionMock(POLICY_V1, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 310);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.fragment(specV2MessageFull);
	}

	@Test
	public void testEnsureV3WhenMultipleVersionsAllowed() throws IOException {
		Session session = createSessionMock(POLICY_V23, 0x5a73a599, 0x27e31597);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 199);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(specV3MessageFull);
		Assert.assertArrayEquals(specV3MessageParts199, msg);
	}
	
	@Test(expected = IOException.class)
	public void testExceedProtocolMaximumNumberOfFragments() throws IOException {
		final String veryLongString = new String(new char[65537]).replace('\0', 'a');
		Session session = createSessionMock(POLICY_V3, 0x5a73a599, 0x27e31597);
		FragmenterInstructions instructions = new FragmenterInstructions(-1,
				OtrFragmenter.computeHeaderV3Size() + 1);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		fragmenter.fragment(veryLongString);
	}
	
	@Test
	public void testFragmentPatternsV3() throws IOException {		
		final Pattern OTRv3_FRAGMENT_PATTERN = Pattern.compile("^\\?OTR\\|[0-9abcdef]{8}\\|[0-9abcdef]{8},\\d{5},\\d{5},[a-zA-Z0-9\\+/=\\?:]+,$");
		final String payload = new String(Base64.encode(RandomStringUtils.random(1700).getBytes("UTF-8")));
		Session session = createSessionMock(POLICY_V3, 0x5a73a599, 0x27e31597);
		FragmenterInstructions instructions = new FragmenterInstructions(-1,
				150);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(payload);
		int count = 1;
		for (String part : msg) {
			Assert.assertTrue(OTRv3_FRAGMENT_PATTERN.matcher(part).matches());
			// Test monotonic increase of part numbers ...
			int partNumber = Integer.parseInt(part.substring(23, 28), 10);
			Assert.assertEquals(count, partNumber);
			count++;
		}
	}
	
	@Test
	public void testFragmentPatternsV2() throws IOException {		
		final Pattern OTRv2_FRAGMENT_PATTERN = Pattern.compile("^\\?OTR,\\d{1,5},\\d{1,5},[a-zA-Z0-9\\+/=\\?:]+,$");
		final String payload = new String(Base64.encode(RandomStringUtils.random(700).getBytes("UTF-8")));
		Session session = createSessionMock(POLICY_V2, 0, 0);
		FragmenterInstructions instructions = new FragmenterInstructions(-1, 150);
		OtrFragmenter fragmenter = new OtrFragmenter(session, instructions);
		String[] msg = fragmenter.fragment(payload);
		int count = 1;
		for (String part : msg) {
			Assert.assertTrue(OTRv2_FRAGMENT_PATTERN.matcher(part).matches());
			// Test monotonic increase of part numbers ...
			String temp = part.substring(5, 11);
			int partNumber = Integer.parseInt(temp.substring(0, temp.indexOf(',')), 10);
			Assert.assertEquals(count, partNumber);
			count++;
		}
	}
	
	private Session createSessionMock(final OtrPolicy policy, final int sender, final int receiver) {
		InstanceTag senderInstance = Mockito.mock(InstanceTag.class);
		Mockito.when(senderInstance.getValue()).thenReturn(sender);
		InstanceTag receiverInstance = Mockito.mock(InstanceTag.class);
		Mockito.when(receiverInstance.getValue()).thenReturn(receiver);
		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getSenderInstanceTag()).thenReturn(senderInstance);
		Mockito.when(session.getReceiverInstanceTag()).thenReturn(receiverInstance);
		Mockito.when(session.getSessionPolicy()).thenReturn(policy);
		return session;
	}
}