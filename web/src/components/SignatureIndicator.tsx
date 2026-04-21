interface SignatureIndicatorProps {
  signatureValid?: boolean
}

export function SignatureIndicator({ signatureValid }: SignatureIndicatorProps) {
  if (signatureValid === undefined || signatureValid === null) {
    return null
  }

  return (
    <span
      className={`inline-flex items-center gap-1 text-[11px] ${
        signatureValid ? 'text-emerald-500' : 'text-red-400'
      }`}
      title={signatureValid ? '签名已验证' : '签名验证失败'}
    >
      {signatureValid ? (
        <svg className="w-3 h-3" viewBox="0 0 16 16" fill="currentColor">
          <path d="M8 16A8 8 0 108 0a8 8 0 000 16zm3.78-9.72a.75.75 0 00-1.06-1.06L7 8.94 5.28 7.22a.75.75 0 00-1.06 1.06l2.25 2.25a.75.75 0 001.06 0l4.25-4.25z" />
        </svg>
      ) : (
        <svg className="w-3 h-3" viewBox="0 0 16 16" fill="currentColor">
          <path d="M8 16A8 8 0 108 0a8 8 0 000 16zM5.354 5.354a.5.5 0 01.707 0L8 7.293l1.939-1.939a.5.5 0 11.707.707L8.707 8l1.939 1.939a.5.5 0 01-.707.707L8 8.707l-1.939 1.939a.5.5 0 01-.707-.707L7.293 8 5.354 6.061a.5.5 0 010-.707z" />
        </svg>
      )}
      {signatureValid ? 'signed' : 'invalid'}
    </span>
  )
}
